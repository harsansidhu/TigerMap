package com.example.odunayo.mapplus;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SelectFriends extends Activity {
    private ArrayList<Friend> friends;     // friends list
    private ArrayList<Friend> contacts = new ArrayList<Friend>(); // contacts list
    private ArrayAdapter<Friend> adapter;  // adapter for list
    private ListView list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_select_friends);

        // fetch existing arraylist of friends
        Bundle bundle = this.getIntent().getExtras();
        friends = bundle.getParcelableArrayList("friends");

        fetchContacts();
        populateListView();
    }

    // action bar menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.select_friends, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        int id = item.getItemId();

        // finish and don't do anything with the selected friends
        if (id == R.id.cancel)
            finish();

        // save selected friends in arraylist and send to the list
        // in previous page
        if (id == R.id.done) {

            // iterate through the list and add selected friends
            for (Friend current : contacts) {
                // if current friend is not in list and is selected, add
                if (current.isSelected() && !friends.contains(current)) {
                    friends.add(current);
                    Toast.makeText(SelectFriends.this, current.getName()
                            + " was added!", Toast.LENGTH_SHORT).show();
                }

                // if current friend is in list and is unselected, remove
                else if (!current.isSelected() && friends.contains(current)) {
                    friends.remove(current);
                    Toast.makeText(SelectFriends.this, current.getName()
                            + " was removed!", Toast.LENGTH_SHORT).show();
                }
            }

            Intent intent = new Intent();
            Bundle b = new Bundle();
            b.putParcelableArrayList("friends", friends);
            intent.putExtras(b);
            setResult(RESULT_OK,intent);
            finish();
        }
        return true;
    }

    // goes through phone contacts and finds people with phone numbers
    public void fetchContacts() {
        Cursor phones = getContentResolver().query(ContactsContract
                .CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
            String name = phones.getString(phones.getColumnIndex
                    (ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex
                    (ContactsContract.CommonDataKinds.Phone.NUMBER));

            // only show USA numbers
            if (phoneNumber.startsWith("+1") || phoneNumber.startsWith("1")
                    || phoneNumber.startsWith("(")) {
                Friend contact = new Friend(name, R.drawable.contacts, phoneNumber);
                contacts.add(contact);
            }
        }
        phones.close();

        // iterate through existing friends list and update checkbox = true
        // for the equivalent contact in the contacts list. Works because
        // equals() doesn't check selected
        for (Friend added : friends) {
            contacts.remove(added);
            contacts.add(added);
        }
        // sort contacts
        Collections.sort(contacts);
    }

    // adds contacts into listview
    private void populateListView() {
        adapter = new MyListAdapter();
        list = (ListView) findViewById(R.id.friend_list);
        list.setAdapter(adapter);
    }

    // adapter for converting friends into listview
    private class MyListAdapter extends ArrayAdapter<Friend> {
        public MyListAdapter() {
            super(SelectFriends.this, R.layout.item_select_friend, contacts);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // make sure we have a view to work with
            View itemView = convertView;
            final ViewHolder holder;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate
                        (R.layout.item_select_friend, parent, false);
                holder = new ViewHolder();

                // assign id for each friend
                holder.name = (TextView) itemView.findViewById(R.id.item_name);
                holder.icon = (ImageView) itemView.findViewById(R.id.item_icon);
                holder.checkbox = (CheckBox) itemView.findViewById(R.id.item_checkbox);
                holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    // when check box is clicked change state of tick
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        int getPosition = (Integer) buttonView.getTag();
                        Friend clickedFriend = contacts.get(getPosition);
                        clickedFriend.setSelected(buttonView.isChecked());
                    }
                });
                itemView.setTag(holder);
                itemView.setTag(R.id.item_name, holder.name);
                itemView.setTag(R.id.item_icon, holder.icon);
                itemView.setTag(R.id.item_checkbox, holder.checkbox);
            }

            // if convertView isnt null
            else
                holder = (ViewHolder) itemView.getTag();

            holder.checkbox.setTag(position);

            // assign details to each id
            holder.name.setText(contacts.get(position).getName());
            holder.icon.setImageResource(contacts.get(position).getIcon());
            holder.checkbox.setChecked(contacts.get(position).isSelected());

            return itemView;
        }
    }

    // class to store the item's details
    public class ViewHolder {
        TextView name;
        ImageView icon;
        CheckBox checkbox;
    }
}
