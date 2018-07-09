ABOUT THIS APP - This Android app was created as an assignment for the Udacity-Google Android Development Nanodegree.  It utilizes the themoviedb.org's API to pull in movie data and display it in. The app consists of two screens that list movies according to the chosen sort (popular or top rated) and a screen that lists the user's favorite movies. A menu allows the user to switch between these three listing screens. If a user clicks on a movie in any of these list screens, they are taken to a screen the shows the movie's details.  The movie details are separated into 3 fragments - main details, reviews, and trailers - which the user can access via a menu at the bottom of the screen.  On the main details screen, the user can click a button to add the movie to their favorites list.

The app uses both AsyncTaskLoaders to load the data from the endpoints and Room, LiveData and ViewModels for the Favorites functionality for learning purposes.  

API KEY - The API key is not included in the app's repository.  The examples URLs used in this readme file will not work.  The user should generate a new API key (create an account with themoviedb.org and then visit the account area to generate a key) and save it in a single line in a text file named key.txt in a folder named assets at /app/src/main/assets/key.txt. It should sit alongside the java and res folders. The app will automatically grab the key from the file.

Project Implementation Guide:
https://docs.google.com/document/d/1ZlN1fUsCSKuInLECcJkslIqvpKlP7jWL2TP9m6UiA6I/pub#h.7sxo8jefdfll

Project Requirements:
https://review.udacity.com/#!/rubrics/67/view

API info:
https://www.themoviedb.org/settings/api

Documentation:
https://developers.themoviedb.org/3/getting-started/introduction

Example API Request (example api key - does not work):
https://api.themoviedb.org/3/movie/550?api_key=db0b3d5274db83389840b9666d851ad0
https://api.themoviedb.org/3/movie/{id}/videos?api_key=db0b3d5274db83389840b9666d851ad0
https://api.themoviedb.org/3/movie/{id}/reviews?api_key=db0b3d5274db83389840b9666d851ad0

Endpoints:
/movie/popular
/movie/top_rated
movie/{id}/videos
movie/{id}/reviews

Attribution:
Icons from https://thenounproject.com/


