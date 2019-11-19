Lendr Backend
=============

BASE_URL = https://zero5nelsonm-lendr.herokuapp.com  

*Note* - All id's (userid, itemid, itemhistoryid) are of type `long`

## Login  

Endpoint = "/login"  

**Accepts:**  
Oauth2 Header info  

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
*Note* - `email` must use standard email format  
Required field(s) = **ALL**  
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
*Note* - Fields that are NOT sent will automatically default to `null`.  
Required field(s) = `itemname`  
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
Send the following to "/items/item"  
```
{
    "itemname": "Chain Saw"
}
```  

Will create an item that looks like the following when you query for the `itemid`, or list all items  
for the user.  
```  
{
    "itemid": long,
    "itemname": String,
    "itemdescription": null,
    "lentto": null,
    "lentdate": null,
    "lendnotes": null
}
``` 

## List all items for a user  

GET  
Endpoint = "/items/items"  

**Accepts:**  
Oauth2 Header info  

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
    },
    {
        "itemid": 9,
        "itemname": "Drill",
        "itemdescription": "Dewalt Drill",
        "lentto": "Allen",
        "lentdate": "November 21, 2019",
        "lendnotes": null
    },
    {
        "itemid": 10,
        "itemname": "Chain Saw",
        "itemdescription": null,
        "lentto": null,
        "lentdate": null,
        "lendnotes": null
    },
    {
        "itemid": 15,
        "itemname": "Drills",
        "itemdescription": "Dewalt Cordless Drill",
        "lentto": "Tyler",
        "lentdate": "May 31, 2019",
        "lendnotes": "Need it back by June 15 for a job."
    }
]
```
