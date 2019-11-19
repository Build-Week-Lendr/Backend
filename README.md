Lendr Backend
=============

BASE_URL = https://zero5nelsonm-lendr.herokuapp.com  

Detailed API Documentation can be found at endpoint: "/v2/api-docs"

*Note* - All id's (userid, itemid, itemhistoryid) are of type `long` and are automatically generated and returned to you.

## Data Models  

**Item**  
```
{
    "itemid": long,
    "itemname": String,
    "itemdescription": String,
    "lentto": String,
    "lentdate": String,
    "lendnotes": String,
    "itemhistories": List<ItemHistory>
    }
```

**ItemHistory** 
```
{
    "itemhistoryid": long,
    "lentto": String,
    "lentdate": String,
    "lendnotes": String,
    "datereturned": String
}
```  
  
**Example JSON**  
```  
{
    "itemid": 9,
    "itemname": "Drill",
    "itemdescription": "Dewalt Drill",
    "lentto": null,
    "lentdate": null,
    "lendnotes": null,
    "itemhistories": [
        {
            "itemhistoryid": 10,
            "lentto": "Allen",
            "lentdate": "November 21, 2019",
            "lendnotes": null,
            "datereturned": "11-19-2019"
        }
    ]
}
```  
  
## Login  

Endpoint = "/login"  

**Accepts:**  
Oauth2 Header info  

**Returns:**  
```
{
    "access_token": String,  
    "token_type": String,  
    "expires_in": int,  
    "scope": String  
}
```

## Logout  

GET  
Endpoint = "/logout"  

**Accepts:**  
Oauth2 Header info  

**Returns:**  
Status 200 OK  

## Create a new user  

POST  
Endpoint = "/createnewuser"  
  
**Accepts:**  
Oauth2 Header info  
Required field(s) = **ALL**  
*Note* - `email` must use standard email format  
```
{
    "username": String,
    "password": String,
    "email": String
}
```

**Returns:**  
Status 201 Created && `userid` in the header under `Location`
```
{
    "access_token": String,  
    "token_type": String,  
    "expires_in": int,  
    "scope": String  
}
```

Alternatively, you can append the aforementioned endpoint with `?returninfo=false` to  
recieve only the header information back and no JSON.  

## Get user information for an authenticated user  

GET  
Endpoint = "/users/getuserinfo"  

**Accepts:**  
Oauth2 Header info  

**Returns:**  
Status 200 OK  
```
{
    "userid": long,
    "username": String,
    "email": String
}
```  

## Create an item  

POST  
Endpoint = "/items/item"  

**Accepts:**  
Oauth2 Header info  
Required field(s) = `itemname`  
*Note* - Fields that are NOT sent with the POST request will automatically default to `null`.  
```
{
    "itemname": String,  
    "itemdescription": String,
    "lentto": String,
    "lentdate": String,
    "lendnotes": String
}
```  

**Returns:**  
Status 201 Created && `itemid` in the header under `Location`  

**Example:**  
With an authenticated user, send the following to "/items/item" as a POST request 
```
{
    "itemname": "Chainsaw"
}
```  

Will create an item that looks like the following when you query for the `itemid`, or list all items  
for the user.  
```  
{
    "itemid": 10,
    "itemname": "Chainsaw",
    "itemdescription": null,
    "lentto": null,
    "lentdate": null,
    "lendnotes": null,
    "itemhistories": []
}
``` 
where `itemhistories` is a List of ItemHistory (See data models at top of README).  

## List all items for a user  

GET  
Endpoint = "/items/items"  

**Accepts:**  
Oauth2 Header info  
*Note* - This endpoint will return a list of Items where `itemhistories` is an empty  
list. If you wish to have `itemhistories` included, append the endpoint with `?returnitemhistory=true`

**Returns:**  
Status 200 OK  
A list of Items for the authenticated user, for example:  
```  
[
    {
        "itemid": 8,
        "itemname": "Chop Saw",
        "itemdescription": "Dewalt Chop Saw",
        "lentto": "Allen",
        "lentdate": "November 21, 2019",
        "lendnotes": null
        "itemhistories": []
    },
    {
        "itemid": 9,
        "itemname": "Drill",
        "itemdescription": "Dewalt Drill",
        "lentto": "Allen",
        "lentdate": "November 21, 2019",
        "lendnotes": null
        "itemhistories": []
    },
    {
        "itemid": 10,
        "itemname": "Chainsaw",
        "itemdescription": null,
        "lentto": null,
        "lentdate": null,
        "lendnotes": null
        "itemhistories": []
    },
    {
        "itemid": 15,
        "itemname": "Drills",
        "itemdescription": "Dewalt Cordless Drill",
        "lentto": "Tyler",
        "lentdate": "May 31, 2019",
        "lendnotes": "Need it back by June 15 for a job."
        "itemhistories": []
    }
]
```
