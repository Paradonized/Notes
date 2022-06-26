# Notes
Notes is an android app made with Android studio and written in Java.<br />
<img alt="GitHub last commit" src="https://img.shields.io/github/last-commit/Paradonized/Ski-Resort?style=plastic">

## Features
* CRUD operations on notes.
* Dates are automatically recorded on the note when it is created or updated.
* Pinned notes cannot be deleted.
* Search by text or/and title of the note.
* Empty note cannot be created.
* No loss of information when the screen is rotated.

## Screenshots
<p align="center">
 <img alt="Home" width="50%" src="https://user-images.githubusercontent.com/85744016/175787010-18175103-27bd-4291-b43f-53659cb0b0ba.png" />

 <img alt="Popup" width="50%" src="https://user-images.githubusercontent.com/85744016/175787370-97e9a318-b846-471d-a57a-54e62db342ac.png" />

 <img alt="Create Note" width="50%" src="https://user-images.githubusercontent.com/85744016/175787015-fd00af1a-9deb-4a1b-83c0-04cb2ea836b2.png" />
 
<img alt="Search Note" width="50%" src="https://user-images.githubusercontent.com/85744016/175787028-728dae1c-8332-4d96-b0a0-eb125e9e76e5.png" />
 
 <img alt="View Note" width="50%" src="https://user-images.githubusercontent.com/85744016/175787042-b7effd98-a07a-4d4d-8d35-32ebae3b2ceb.png" />
</p>

## Technical Part
* RecyclerView is used in order to visualize all notes. 
* RelativeLayout is used in order to fit as many notes as possible and to lessen the empty space between notes.
* With CardView is visualized each note with its title (if it has one), content and date of creation.
* Popup menu is used for deleting or pinning/unpinning notes.
* RoomDB is used as our database.

## Future improvements
* Profiles.
* Tags.
* Filter by tags.
* Side menu.
