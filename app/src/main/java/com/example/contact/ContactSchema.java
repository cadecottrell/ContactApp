package com.example.contact;

//From in Class, the Database schema such that it may be accessed from anywhere.
public class ContactSchema
{
    public static final class ContactTable
    {
        public static final String TABLE_NAME = "contact_table";

        public static final class Fields
        {
            public static final String COL1 = "ID";
            public static final String COL2 = "record";
            public static final String COL3 = "Address1";
            public static final String COL4 = "Address2";
            public static final String COL5 = "City";
            public static final String COL6 = "State";
            public static final String COL7 = "Zipcode";

        }
    }
}
