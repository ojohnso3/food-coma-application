# FoodCOMA: CS32 Term Project 2020
###### Cameron Wenzel, Olivia Johnson, Morgann Thain, Annabeth Stokely

## How to Build and Run
to build use `mvn package`, then run using `./run --gui`

## Design Decisions:
### API:
 We decided to use an API instead of a set Recipe Database in 
 order to be able to dynamically load and cache recipes. We have 
 a Database package which deals with getting calls from the API, 
 Databases and Caches. We have Recipe Deserializers in order to 
 convert from the API JSON objects into nodes which we are 
 able to use in our KD Tree, as well as the other classes 
 within our package. We also implemented a FieldParser class 
 which allows for the handling of API, Database or Cache calls.

### Caching: 
We cache recipes that are inputted from the API. We also cache 
queries, such that if a user inputs the exact same query with 
the same parameters, we will not check the API again and 
instead rely on our Cache. In addition, all recommended 
recipes come from the Database, unless there is a database 
error, in which case the API is called again. Also, we do 
have API limits. Therefore, in the case that we run over our 
allowed number of API calls, we use the cache to find similar 
recipes to output the best possible results to the user 
seamlessly. If this website were to be deployed professionally,
 we would of course purchase an API package which would be 
 more appropriate. 
 
### Recommendation Algorithm:
Our algorithm is based on mapping each recipe into Euclidean
Space based on their nutritional information and storing their
locations in a K-D tree. This allows us to find the recipes
closest to what we determine to be an "ideal point". We 
calculated this using the locations of recipes in the 
user's search history. 

First, we take the recipes in the user's history and make them
into nodes. We take their values for key nutrients we've chosen 
and we let those be the coordinates of their position. Then, we 
normalize the coordinates representing each nutrient, mapping
them onto [0,1] such that the lowest value in the dataset is 0 and 
the highest is 1 and the ratios between values are maintained. 
However, because we consider certain nutrients to be more important, 
like calories and macronutrients such as protein, and because the
user has marked certain nutrients as more important to them.

We implemented a K-D tree algorithm which maps recipes based 
off their nutrient contents. This mapping is changed each 
time the user makes a new query. In addition, the target
 node of our K-D tree changes based off all previously 
 clicked recipes. So, if the user clicks on lots of high
 -sugar recipes, for instance, then we will recommend other
  high-sugar recipes. Our recommender algorithm is based off
   the idea that people will gravitate towards recipes with 
   similar nutrients to other recipes, rather than 
    flavors. This is what makes foodCOMA unique. We do not 
    simply recommend similar flavors, but instead new flavors 
    and recipe ideas with similar nutritional content. We 
    chose to make this recommendation from 100 recipes. 
    This could be refined to include more or less recipes, 
    depending on the importance put on variety, speed and 
    API limitations. We decided that 100 recipes was 
    appropriate for this use. 

### User Survey:
We also implemented a User Survey, which changes the weights 
of the KD Tree nodes based off normalization values. 

### User:
We implemented a User database. The user is stored 
in a database. We also use encryption for the passwords to 
ensure they are safe.

### GUI:
Our GUI class generates pages based off post and get requests. 
Each page has its own get request, and those which are 
interactive have post requests. The Search and Recipe pages 
use JSON objects to communicate between the front and back 
end, and this was the preferred method for us to use because 
it offers maximum flexibility. We also had to implement 
arrays within the JSON, because JSONs are unordered. 
Therefore, we are able to preserve order in the JSON by 
using pre-sorted arrays.

### foodCOMA score:
We implemented a foodCOMA score based off the distance from 
our target node, which is dynamic. Therefore, based off making
 new queries and creating new KD Trees with new target nodes, 
 the foodCOMA scores adapt after the user clicks on new 
 recipes. By nature of our KD Tree, the score also takes 
 into account the survey. 

## Accreditations
We use the following Recipe Search API from EDAMAM:
https://developer.edamam.com/edamam-docs-recipe-api

This database's tagging isn't perfect; for example, checking
a vegetarian dietary preference then searching up beef will
give you a bunch of non-vegetarian beef options. There are 
other ways to get conflicts, so in general results might
not be as expected.

: We use a BCrypt solution in this project from: 
https://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/

We gathered HTML, CSS and JS help from w3schools:
https://www.w3schools.com/

## Checkstyle Errors:
I have a few extra method overrides in Account in case a user wanted to 
login through the repl or some other cases, so there are some
unused functions errors. 

It sometimes gets mad about creating "new ArrayList<>()" multiple
times, and I wasn't going to make a separate function for that.

The BCrypt solution has many checkstyle errors and uses tabs
instead of spaces (666 errors!!!), and we did not mess with it.

## Thank You!
Thank you Tim, HTAs and UTAs for a fantastic semester! This was a difficult project for us due to the circumstances, and we realize that it is equally hard on the staff of CS32. Special thanks to Gabi, who was supportive of all our ideas throughout this project. We really couldn't have made it through without such a wonderful and thoughtful TA!
