Lendr Backend
=============
<h3 id="tableofcontents">Table of Contents</h3>  

*   [Data Models](#datamodels)
*   [Basic Api Info](#apiinfo)
*   Open Endpoints
    *   [Login](#login)
*   Logout Endpoints
    *   [Logout](#logout)
*   User Endpoints
    *   [Create New User](#createnewuser)
    *   [Get User Information](#getuserinfo)
*   Item Endpoints
    *	[Create Item](#createitem)
    *   [List Items](#listitems)
    *   [Get Item by Id](#getitembyid)
        *   [Submit Item as Being Returned](#beingreturned)
    *   [Update Item](#updateitem)
    *   [Delete Item](#deleteitem)
*   ItemHistory Endpoints
    *	[List ItemHistory for an Item](#listitemhistory)
    *	[Get ItemHistory by Id](#getitemhistorybyid)
    *	[Create ItemHistory for an Item](#createitemhistory)
    *	[Update ItemHistory](#updateitemhistory)
    *   [Delete ItemHistory](#deleteitemhistory)

<h2 id="datamodels">Data Models</h2>  

[Back to Table of Contents](#tableofcontents)  

*Note* - All id's (userid, itemid, itemhistoryid) are of type `long` and are automatically generated and returned to you.  

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

<h2 id="apiinfo">Basic Api Info</h2>  

[Back to Table of Contents](#tableofcontents)  

BASEURL: https://zero5nelsonm-lendr.herokuapp.com  
  
Documentation Website: https://zero5nelsonm-lendr.herokuapp.com/swagger-ui.html  
Documentation Endpoint: "/v2/api-docs"  

<h2 id="login">Login</h2>  

[Back to Table of Contents](#tableofcontents)  

POST  
Endpoint: "/login"  

**Accepts:**  
Oauth2 Header info  

**Returns:**  
Status 200 OK  
```
{
    "access_token": String,  
    "token_type": String,  
    "expires_in": int,  
    "scope": String  
}
```

<h2 id="logout">Logout</h2>  

[Back to Table of Contents](#tableofcontents)  

GET  
Endpoint: "/logout"  

**Accepts:**  
Oauth2 Header info  

**Returns:**  
Status 200 OK  

<h2 id="createnewuser">Create New User</h2>  

[Back to Table of Contents](#tableofcontents)  

POST  
Endpoint: "/createnewuser"  
  
**Accepts:**  
Oauth2 Header info  

Required field(s): **ALL**  
*Note* - `email` must use standard email format  
```
{
    "username": String,
    "password": String,
    "email": String
}
```

**Returns:**  
Status 201 Created && `userid` in the return header under `Location`
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

<h2 id="getuserinfo">Get User Information</h2>  

[Back to Table of Contents](#tableofcontents)  

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

<h2 id="createitem">Create Item</h2>  

[Back to Table of Contents](#tableofcontents)  

POST  
Endpoint = "/items/item"  

**Accepts:**  
Oauth2 Header info  

*    Body requires an Item  
*    Required field(s): `itemname`  
*    *Note* - Fields that are NOT sent with the POST request will automatically default to `null` when the new Item is created.  
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
Sending a POST request to `BASEURL/items/item` with the following data 
```
{
    "itemname": "Chainsaw"
}
```  

Will create an item that looks like the following when you send a GET request to `BASEURL/items/item/{itemid}` for  
the newly created item.  
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
where `itemhistories` is a List of ItemHistory (See Data Models above).  

<h2 id="listitems">List Items</h2>  

[Back to Table of Contents](#tableofcontents)  

GET  
Endpoint: "/items/items"  

**Accepts:**  
Oauth2 Header info  

*Note* - By default, this endpoint will return a list of Items where `itemhistories` is an empty  
list. If you wish to have the Item's `itemhistories` included, append the endpoint with `?returnitemhistory=true`.

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

<h2 id="getitembyid">Get Item by Id</h2>  

[Back to Table of Contents](#tableofcontents)  

GET  
Endpoint: "/items/item/{itemid}"  

**Accepts:**  
Oauth2 Header info  

**Returns:**  
Status 200 OK  
The requested item, for example:  
```  
{
    "itemid": 8,
    "itemname": "Chop Saw",
    "itemdescription": "Dewalt Chop Saw",
    "lentto": "Jake",
    "lentdate": "February 13, 2019",
    "lendnotes": "Last time I am lending to Jake...",
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

<h3 id="beingreturned">Submit Item as Being Returned</h3>  

Alternatively, you can append the Get Item By Id endpoint with `?beingreturned=true`, and  
it will do some magic for you.  

The Item's field's for `lentto`, `lentdate`, and `lendnotes` will be transfered to a new ItemHistory  
for that Item, and then those fields for the Item will be set to `null`. The `datereturned` for the  
new ItemHistory will automatically be generated.

**Example:**  
Sending a GET request to `BASEURL/items/item/8` returns:  
```
{
    "itemid": 8,
    "itemname": "Chop Saw",
    "itemdescription": "Dewalt Chop Saw",
    "lentto": "Jake",
    "lentdate": "February 13, 2019",
    "lendnotes": "Last time I am lending to Jake...",
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

Sending a GET request to `BASEURL/items/item/8?beingreturned=true` returns:  
```
{
    "itemid": 8,
    "itemname": "Chop Saw",
    "itemdescription": "Dewalt Chop Saw",
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
        },
        {
            "itemhistoryid": 11,
            "lentto": "Jake",
            "lentdate": "February 13, 2019",
            "lendnotes": "Last time I am lending to Jake...",
            "datereturned": "11-20-2019"
        }
    ]
}
```  

<h2 id="updateitem">Update Item</h2>  

[Back to Table of Contents](#tableofcontents)  

PUT  
Endpoint: "/items/item/{itemid}"  

**Accepts**  
Oauth2 Header info  

*    Body requires an Item  
*    Required field(s): **NONE**  
*    *Note* - You can send only the individual field(s) needing to be updated if you wish.  
```  
{
    "itemname:" String,
    "itemdescription:" String,
    "lentto": String,
    "lentdate": String,
    "lendnotes": String
}
```  

**Returns:**  
Status 200 OK  

**Example:**  
Sending a PUT request to `BASEURL/items/item/8` with the following data  
```  
{
	"lentto": "Katlin",
	"lentdate": "August 5, 2019"
}
```  
Will update the item. Then when you send a GET request to `BASEURL/items/item/8` for  
the newly updated item it will look like:  
```  
{
    "itemid": 8,
    "itemname": "Chop Saw",
    "itemdescription": "Dewalt Chop Saw",
    "lentto": "Katlin",
    "lentdate": "August 5, 2019",
    "lendnotes": null,
    "itemhistories": [
        {
            "itemhistoryid": 10,
            "lentto": "Allen",
            "lentdate": "November 21, 2019",
            "lendnotes": null,
            "datereturned": "11-19-2019"
        },
        {
            "itemhistoryid": 11,
            "lentto": "Jake",
            "lentdate": "February 13, 2019",
            "lendnotes": "Last time I am lending to Jake...",
            "datereturned": "11-20-2019"
        }
    ]
}
```  

<h2 id="deleteitem">Delete Item</h2>  

[Back to Table of Contents](#tableofcontents)  

DELETE  
Endpoint: "/items/item/{itemid}"  

**Accepts:**  
Oauth2 Header info  

**Returns:**  
Status 200 OK  

<h2 id="listitemhistory">List ItemHistory for an Item</h2>  

[Back to Table of Contents](#tableofcontents)  

GET  
Endpoint: "/itemhistory/item/{itemid}"  

**Accepts:**  
Oauth2 Header info  

**Returns:**  
Status 200 OK  
A list of ItemHistory for the Item, for example:  
```  
[
    {
        "itemhistoryid": 10,
        "lentto": "Allen",
        "lentdate": "November 21, 2019",
        "lendnotes": null,
        "datereturned": "11-19-2019"
    },
    {
        "itemhistoryid": 11,
        "lentto": "Jake",
        "lentdate": "February 13, 2019",
        "lendnotes": "Last time I am lending to Jake...",
        "datereturned": "11-20-2019"
    }
]
```  

<h2 id="getitemhistorybyid">Get ItemHistory by Id</h2>  

[Back to Table of Contents](#tableofcontents)  

GET  
Endpoint: "/itemhistory/{itemhistoryid}"  

**Accepts:**  
Oauth2 Header info  

**Returns:**  
Status 200 OK  
The requested ItemHistory, for example:  
```  
{
    "itemhistoryid": 11,
    "lentto": "Jake",
    "lentdate": "February 13, 2019",
    "lendnotes": "Last time I am lending to Jake...",
    "datereturned": "11-20-2019"
}
```  

<h2 id="createitemhistory">Create ItemHistory for an Item</h2>  

[Back to Table of Contents](#tableofcontents)  

POST  
Endpoint = "/itemhistory/item/{itemid}"  

**Accepts:**  
Oauth2 Header info  

*    Body requires an ItemHistory  
*    Required field(s): **NONE**  
*    *Note* - Fields that are NOT sent with the POST request will automatically default to `null` when the new ItemHistory is created.  
```
{
    "lentto": String,
    "lentdate": String,
    "lendnotes": String,
    "datereturned": String
}
```  

**Returns:**  
Status 201 Created && `itemhistoryid` in the header under `Location`  

**Example:**  
Sending a POST request to `BASEURL/itemhistory/item/8` with the following data  
```
{
    "lentto": "Alice",
    "lentdate": "August 23, 2019"
}
```  

Will create an item that looks like the following when you send a GET request to `BASEURL/itemhistory/12` for the newly created ItemHistory.  
```  
{
    "itemhistoryid": 12,
    "lentto": "Alice",
    "lentdate": "August 23, 2019",
    "lendnotes": null,
    "datereturned": null
}
```  

<h2 id="updateitemhistory">Update ItemHistory</h2>  

[Back to Table of Contents](#tableofcontents)  

PUT  
Endpoing: "/itemhistory/{itemhistoryid}"  

**Accepts:**  
Oauth2 Header info  

*    Body requires an ItemHistory  
*    Required field(s): **NONE**  
*    *Note* - You can send only the individual field(s) needing to be updated if you wish.  
```  
{
    "lentto": String,
    "lentdate": String,
    "lendnotes": String,
    "datereturned": String
}
```  

**Returns:**  
Status 200 OK  

**Example:**  
Sending a PUT request to `BASEURL/itemhistory/12` with the following data  
```  
{
	"lendnotes": "Returned it soaking wet!"
}
```  
Will update the ItemHistory. Then when you send a GET request to `BASEURL/itemhistory/12` for  
the newly updated ItemHistory, it will look like:  
```  
{
    "itemhistoryid": 12,
    "lentto": "Alice",
    "lentdate": "August 23, 2019",
    "lendnotes": "Returned it soaking wet!",
    "datereturned": null
}
```  

<h2 id="deleteitemhistory">Delete ItemHistory</h2>  

[Back to Table of Contents](#tableofcontents)  

DELETE  
Endpoint: "/itemhistory/{itemhistoryid}"  

**Accepts:**  
Oauth2 Header info  

**Returns:**  
Status 200 OK  
