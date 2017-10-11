# Grocer TODO
  
### Visual Changes
- [ ] Change the list item layout so the Name TextView is centered vertically if the entry has no note / quantity.
- [ ] Move sort button from menu to own button in ActionBar / Toolbar (Button triggers dropdown of sort-types)

### Functional Changes
- [ ] Disable long press to drag in favor of a long press action to trigger a Fragment where the user can edit the entry.
- [x] Implement an optional quantity field to the AddDialog / Database
- [ ] Disallow adding entries with no name, Input validation in general
- [x] Store and update the entry's checked field in the database
- [ ] Establish the Settings Fragment (See section below)
- [ ] Add bug reporter to option menu - Opens new fragment
  
    
  


## Settings

| Setting Name | Preference Type | Value(s) | Desc. |
| ------------ | --------------- | -------- | ----- |
| pref_enable_loc | CheckBoxPreference | N/A | When enabled, allows location services. Used for quick launching if at grocery store. |
| pref_enable_clear_checked | CheckboxPreference | N/A | When enabled, deletes checked entries on reload / sort. |
