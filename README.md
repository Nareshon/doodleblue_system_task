# Railway Reservation

Spring APIs for Railway Reservation.

Feature :
    This api can be used to allocate tickets with respect to your age,gender and the seating arrangement.

 Logic :- 
    1)  Lower berth will be allocated for persons whose age is above 60 and ladies with children if available
    2)  Side-lower berths will be allocated for RAC passengers
    3)  Tickets will be filled in the order of s1,s2,s3,s4
    4)  If the coach has more than 4 men and no women, then the ladies will be re-allocated to the next coach.
    5)  If the waiting-list ticket count goes above 5, ‘No tickets available will be displayed
    
  Data captured :-
    1)  Name
    2)  Age
    3)  Gender
    4)  Berth Preference
  
  Database used :-
###     MYSQL

##  Note
Import the postman collection json file from PostManCollection folder inside the project.
    
## Requirements

1. java >= 8.x
2. mysql

## Setup

#### Step-1:
Install Spring Tool Suite

#### Step-2:
Clone the repository and update the project

#### Step-3:

1. Create database railway

#### Step-4 :

Run the project
