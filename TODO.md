# Grocer TODO
  
### Visual Changes
- [ ] Change the list item layout so the Name TextView is centered vertically if the entry has no note / quantity.
- [ ] Add quantity to item layout.
- [ ] Move FAB so it doesn't block checkbox of bottom list items. (Setting for position maybe?)
- [ ] Move sort button from menu to own button in ActionBar / Toolbar (Button triggers dropdown of sort-types)
- [ ] Add instructional background if grocery list is empty.

### Functional Changes
- [x] Disable long press to drag in favor of a long press action to trigger a Fragment where the user can edit the entry.
- [x] Solve repetition in the two DialogFragment classes
- [x] Implement an optional quantity field to the AddDialog / Database
- [x] Disallow adding entries with no name, Input validation in general
- [x] Store and update the entry's checked field in the database
- [ ] Establish the Settings Fragment (See section below)
- [ ] Add bug reporter to option menu - Opens new fragment
- [ ] Add a help menu to the option menu - Opens new fragment

### Authentication and Firebase
- [ ] Enable Firebase Authentication and Realtime Database. When a user logs in, they unlock the ability to store their lists in the cloud, as well as share their lists / recipes with other users.
- [ ] Login / Logoff button on Navigation Panel as well as Settings Fragment
  
    
  


## Settings

| Setting Name | Preference Type | Value(s) | Desc. |
| ------------ | --------------- | -------- | ----- |
| pref_enable_loc | CheckBoxPreference | N/A | When enabled, allows location services. Used for quick launching if at grocery store. |
| pref_enable_clear_checked | CheckboxPreference | N/A | When enabled, deletes checked entries on reload / sort. |
| pref_logon | Preference | N/A | Logon / Logoff button. Allows the user to share lists and store them on the cloud. |
