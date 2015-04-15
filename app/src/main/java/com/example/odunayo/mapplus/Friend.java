package com.example.odunayo.mapplus;


import android.os.Parcel;
import android.os.Parcelable;

public class Friend implements Comparable<Friend>, Parcelable {
    private String name;        // name of friend
    private int icon;           // icon of friend
    private String number;      // phone number of friend
    private boolean selected;   // checkbox boolean

    public Friend(String name, int icon, String number) {
        this.name = name;
        this.icon = icon;
        this.number = number;
    }

    /*Created for testing purposes*/
    public Friend(String name) {
        this.name = name;
        //this.photo = photo;
        //this.requestCode = requestCode;
    }

    // gets name
    public String getName() {
        return name;
    }

    // set name
    public void setName(String name) {
        this.name = name;
    }

    // get photo
    public int getIcon() {
        return icon;
    }

    // get number
    public String getNumber() {
        return number;
    }

    // checkbox booleans
    public boolean isSelected() {
        return selected;
    }

    // check if selected
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    // comparable for friend listing in alphabetical order
    @Override
    public int compareTo(Friend another) {
        return this.getName().compareTo(another.getName());
    }

    // equals method
    @Override
    public boolean equals(Object x) {
        if (this == x) return true;
        if (x == null) return false;
        if (this.getClass() != x.getClass()) return false;

        Friend that = (Friend) x;
        return (this.name.equals(that.name)) &&	((this.icon == that.icon) &&
                (this.number.equals(that.number)));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeInt(icon);
        out.writeString(number);
        out.writeByte((byte) (selected ? 1 : 0));     //if myBoolean == true, byte == 1
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Friend> CREATOR = new Parcelable.Creator<Friend>() {
        public Friend createFromParcel(Parcel in) {
            return new Friend(in);
        }

        public Friend[] newArray(int size) {
            return new Friend[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Friend (Parcel in) {
        name = in.readString();
        icon = in.readInt();
        number = in.readString();
        selected = in.readByte() != 0;
    }
}
