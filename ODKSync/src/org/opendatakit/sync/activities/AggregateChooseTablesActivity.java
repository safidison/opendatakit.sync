/*
 * Copyright (C) 2012 University of Washington
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.opendatakit.sync.activities;

import org.opendatakit.common.android.data.TableProperties;
import org.opendatakit.sync.R;
import org.opendatakit.sync.TableFileUtils;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AggregateChooseTablesActivity extends ListActivity {

  private String appName;
  private ListView tablesView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    appName = getIntent().getStringExtra(Aggregate.INTENT_KEY_APP_NAME);
    if ( appName == null ) {
      appName = TableFileUtils.getDefaultAppName();
    }
    setContentView(R.layout.aggregate_choose_tables_activity);

    setListAdapter(new ArrayAdapter<TableProperties>(this,
        android.R.layout.simple_list_item_multiple_choice,
        getServerDataTables()));

    final ListView listView = getListView();

    listView.setItemsCanFocus(false);
    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    int count = listView.getCount();
    for (int i = 0; i < count; i++) {
      TableProperties tp = (TableProperties) listView.getItemAtPosition(i);
      if (tp.isSetToSync()) {
        listView.setItemChecked(i, true);
      }
    }
  }

  /*
   * This should only display the data tables that are in the server KVS.
   * An invariant that must be maintained is that any table in the server KVS
   * must also have an "isSetToSync" entry in the sync KVS.
   */
  private TableProperties[] getServerDataTables() {
    return TableProperties.getTablePropertiesForAll(this, appName);
  }

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    ListView listView = getListView();
    TableProperties tp =
        (TableProperties) listView.getItemAtPosition(position);
    boolean wantToSync;
    if (tp.isSetToSync()) {
      wantToSync = false;
    } else {
      wantToSync = true;
    }
    tp.setIsSetToSync(wantToSync);
  }
}
