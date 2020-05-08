# cs0320 Term Project 2020

**Team Members:** 
- Annabeth Stokely, Cameron Wenzel, Morgann Thain, Olivia Johnson

**Team Strengths and Weaknesses:** 

*Annabeth Stokely:*
- Strengths: Debugging, Design 
- Weaknesses: Starting with hard parts of the program, Testing

*Cameron Wenzel:*
- Strengths: Starting early, Java, Work Ethic
- Weaknesses: Likes working on his own, Less communicative

*Morgann Thain:*
- Strengths: Structure/Outline/Abstraction, Deep Learning
- Weaknesses: Testing well, Spending too much time on small details

*Olivia Johnson:*
- Strengths: Translating ideas into code, Communicative, Flexible
- Weaknesses: Not spending enough time thinking through design before coding, Indecisive

**Project Idea(s):** Three ideas included below (3/2)
### Idea 1
**Project Idea:** Interface connecting C@B, Critical Review, and Focal Point (concentration requirements) for a convenient and easy-to-use platform for academic planning


**Problem to Solve:** It is difficult to plan your course load according to concentration requirement + academic interests due to the lack of coordination/communication between C@B, Critical Review, and Focal Point.


**Requirements**
- Gathers data from C@B, Critical Review, Focal Point, Bulletin, + other Brown sites
- Structures courses on C@B and Critical Review by a concentration into an outline of sorts
- Presents the most recommended courses for each requirement slot
- Sorts repeated occurrences of courses, and is more efficient at organizing by relevant fields such ratings
- Contains a course recommendation algorithm personally tailored to the class history, concentration, and academic preferences of the individual

**Challenges**
- Data congregation and security when scraping data from Brown sites
- Presenting the data cleanly and with a easy-to-use interface (i.e. intuitive and comprehensive GUI)

**Other Components**
- Possibly using Topological Sorting algorithm to model the order of courses that could be taken for each concentration

Approved - less focus on scraping and more focus on an algorithm for ranking courses

### Idea 2
**Project Idea:** Cooking application that recommends recipes/dishes based on your current food supply and personal taste, keeps track of your inventory, and reminds you to go grocery shopping when supplies run low with a recommended shopping list.

**Problem to Solve:** It is difficult to quickly find recipes on the Internet that are well-suited to both your personal food palate and your needs (time, ingredients, cooking equipment).

**Requirements**

- Takes in inputed ingredients + other desired qualifiers (time, quantity, etc) and outputs recommended recipes
- Uses a compiled database of recipes as well as user's food history for its recommendation algorithm
- Also presents information about contents of recipes (useful for allergy + dietary restrictions)
- Produces a recommended shopping list when user's food supply runs low + estimates price of shopping trip based on contents
- Keeps track of food inventory (past + present) both for use in recipe recommendation and for shopping list
- Export recipes from application to share with friends via message/email

**Challenges**
- Finding usable databases online that are well-suited to our plans for the cooking application
- Data congregation and security when scraping data from these cooking/recipe sites
- Presenting the data cleanly and with a easy-to-use interface (i.e. intuitive and comprehensive GUI)

**Other Components**
- Either a content-based algorithm where we create a profile of the user's preferences and use this information to recommend new recipes, or a collaborative system where we ask users to rate recipes and recommend them based on these ratings. These methods are sourced from [here](https://en.wikipedia.org/wiki/Recommender_system#Approaches).

Approved - algorithm for combining ingredients and ranking dishes should be the main component

### Idea 3
**Project Idea:** Car-pool University, an application that coordinates ride-share trips to & from common destinations (e.g. airport/train stations) with other college students.

**Problem to Solve:** It is difficult for college students to get to major destinations in a cheap and convenient way. This application would provide an effective platform to coordinate trips with other students to reduce the cost, make the trips safer (no longer traveling alone), and even provide the opportunity to make a new friend!

**Requirements**

- Maintains a user network with individual logins/passwords for each user
- Only allows user to register with valid university email (ensures user base is only college students)
- Stores data of all the times that users want to make a trip
- Matches similar source/destination locations and travel times to coordinate trips
- Also matches users based on preferred ride-share application for trip
- Calculates price splitting and possibly has ability to connect to payment platform (e.g. Venmo)
- Has the ability to report users for inappropriate conduct

**Challenges**
- User security + password hashing for each account
- Safety of experience - still could have issues with verifying that user is a legitimate college student
- Efficiently coordinating trips from inputed trips with similar (but not exactly the same) addresses
- Finding usable location databases to map trips / coordinating with ride-share applications
- Presenting the information cleanly and with a easy-to-use interface (i.e. intuitive and comprehensive GUI)

**Other Components**
- We will use a shortest path algorithm such as A* to match destinations and meeting points, taking into account meeting times as well. 

Rejected - common idea with few interesting design decisions

Note: you do not need to resubmit final project ideas. 


### Project Mentor

**Mentor TA:** Gabi, gabriella_vogel-freedman@brown.edu

## Meetings
_On your first meeting with your mentor TA, you should plan dates for at least the following meetings:_

**Specs, Mockup, and Design Meeting:** April 3rd

**4-Way Checkpoint:** _(Schedule for on or before April 23)_

**Adversary Checkpoint:** _(Schedule for on or before April 29 once you are assigned an adversary TA)_

## How to Build and Run
to build use `mvn package`, then run using `./run --gui`

## Recipe Database API
**The database's tagging isn't perfect; for example, checking
a vegetarian dietary preference then searching up beef will
give you a bunch of non-vegetarian beef options. There are 
other ways to get conflicts...so in general results might
not be as expected.


Accreditations: We use a BCrypt solution in this project from: 
https://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/

## Checkstyle Errors:
I have a few extra method overrides in Account in case a user wanted to 
login through the repl or some other cases, so there are some
unused functions errors. 

It sometimes gets mad about "new ArrayList"

The BCrypt solution has some checkstyle errors and uses tabs
instead of spaces but I didn't want to mess with it.