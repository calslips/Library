# Library REST API

This is a simple web API that is connected to a database and allows users to:
- create a library account in order to borrow books
- add new books to the library for others to borrow
- search for books within the library by its title/name/id
- sign books out of the library to read
- return books when they are finished reading them
- delete their accounts if they no longer wish to use the library

## Library Database ERD

The database consists of two entities, Books and Users, in a many-to-one 
relationship:

<p align="center">
    <img 
        src="https://i.ibb.co/YhkLw7c/Database-ERD-Library.jpg" 
        alt="Library database entity relationship diagram"
    >
</p>

# REST API

The Library REST API endpoints are described below.

## Create an Account

### Request

`POST /users/`

    curl -i -H 'Accept: application/json' -d '{"username":"sirreadsalot"}' http://localhost:8080/users/

### Successful Response

    HTTP/1.1 201 Created
    Date: Sun, 01 Oct 2023 18:00:16 GMT
    Content-Type: application/json
    Content-Length: 45

    {
        "userId":68222997,
        "username":"sirreadsalot"
    }

### Unsuccessful Response

If the username is already in use, the input username is empty, or the request body is malformed.

    HTTP/1.1 400 Bad Request
    Date: Sun, 01 Oct 2023 18:00:20 GMT
    Content-Type: text/plain
    Content-Length: 0

## Add a Book

### Request

`POST /books/`

    curl -i -H 'Accept: application/json' -d '{"title":"the long walk","author":"richard bachman"}' http://localhost:8080/books/

### Successful Response

    HTTP/1.1 201 Created
    Date: Sun, 01 Oct 2023 18:06:02 GMT
    Content-Type: application/json
    Content-Length: 88

    {
        "bookId":1785217924,
        "title":"the long walk",
        "author":"richard bachman",
        "signedOutBy":0
    }

### Unsuccessful Response

If the title and/or author is empty, or the request body is malformed.

    HTTP/1.1 400 Bad Request
    Date: Sun, 01 Oct 2023 18:06:09 GMT
    Content-Type: text/plain
    Content-Length: 0


## Get All Books

### Request

`GET /books/`

     curl -i -H 'Accept: application/json' http://localhost:8080/books/

### Response

    HTTP/1.1 200 OK
    Date: Sun, 01 Oct 2023 17:07:44 GMT
    Content-Type: application/json
    Content-Length: 2

    []

## Get All Books By Title

### Request

`GET /books?title={titleName}`

     curl -i -H 'Accept: application/json' http://localhost:8080/books?title=joyland

### Response

    HTTP/1.1 200 OK
    Date: Sun, 01 Oct 2023 17:17:23 GMT
    Content-Type: application/json
    Content-Length: 161

    [
        {
            "bookId":690520471,
            "title":"joyland",
            "author":"stephen king",
            "signedOutBy":0
        },
        {
            "bookId":1076056349,
            "title":"joyland",
            "author":"emily schultz",
            "signedOutBy":0
        }
    ]

## Get All Books By Author

### Request

`GET /books?author={authorName}`

     curl -i -H 'Accept: application/json' http://localhost:8080/books?author=stephen+king

### Response

    HTTP/1.1 200 OK
    Date: Sun, 01 Oct 2023 17:28:43 GMT
    Content-Type: application/json
    Content-Length: 159

    [
        {
            "bookId":690520471,
            "title":"joyland",
            "author":"stephen king",
            "signedOutBy":0
        },
        {
            "bookId": 1989450725,
            "title": "misery",
            "author": "stephen king",
            "signedOutBy": 0
        }
    ]

## Get All Books By Title and Author

### Request

`GET /books?title={titleName}&author={authorName}`

     curl -i -H 'Accept: application/json' http://localhost:8080/books?title=misery&author=stephen+king

### Response

    HTTP/1.1 200 OK
    Date: Sun, 01 Oct 2023 17:36:27 GMT
    Content-Type: application/json
    Content-Length: 80

    [
        {
            "bookId": 1989450725,
            "title": "misery",
            "author": "stephen king",
            "signedOutBy": 0
        }
    ]

## Get a Book By Its Unique ID

### Request

`GET /books/{id}`

     curl -i -H 'Accept: application/json' http://localhost:8080/books/1076056349

### Successful Response

    HTTP/1.1 200 OK
    Date: Sun, 01 Oct 2023 17:38:41 GMT
    Content-Type: application/json
    Content-Length: 80

    {
        "bookId":1076056349,
        "title":"joyland",
        "author":"emily schultz",
        "signedOutBy":0
    }

### Unsuccessful Response

If there is no book associated with the id path parameter.

    HTTP/1.1 404 Not Found
    Date: Sun, 01 Oct 2023 17:38:45 GMT
    Content-Type: text/plain
    Content-Length: 0

## Sign Out OR Return a Book

### Request

`PATCH /books/{id}`

     curl -i -H 'Accept: application/json' -d '{"userId":68222997,"username":"sirreadsalot"}' -X PATCH http://localhost:8080/books/1076056349

### Successful Response

If signing out a book that is available.

    HTTP/1.1 200 OK
    Date: Sun, 01 Oct 2023 18:40:59 GMT
    Content-Type: application/json
    Content-Length: 87

    {
        "bookId":1076056349,
        "title":"joyland",
        "author":"emily schultz",
        "signedOutBy":68222997
    }

If the user is returning a book that they have signed out.

    HTTP/1.1 200 OK
    Date: Sun, 01 Oct 2023 18:41:43 GMT
    Content-Type: application/json
    Content-Length: 80

    {
        "bookId":1076056349,
        "title":"joyland",
        "author":"emily schultz",
        "signedOutBy":0
    }

### Unsuccessful Response

If the user attempts to sign out a book that is currently signed out by another user, 
or the request body is malformed.

    HTTP/1.1 400 Bad Request
    Date: Sun, 01 Oct 2023 17:43:06 GMT
    Content-Type: text/plain
    Content-Length: 0

## Delete User Account

### Request

`DELETE /users/{id}`

     curl -i -H 'Accept: application/json' -d '{"userId":1426351097,"username":"notareader"}' -X DELETE http://localhost:8080/users/1426351097

### Successful Response

If a user is deleting their own account.

    HTTP/1.1 200 OK
    Date: Sun, 01 Oct 2023 19:01:02 GMT
    Content-Type: application/json
    Content-Length: 45

    {
        "userId":1426351097,
        "username":"notareader"
    }

### Unsuccessful Response

If the user attempts to delete their account when they have a book signed out, or the request body is malformed.

    HTTP/1.1 400 Bad Request
    Date: Sun, 01 Oct 2023 18:59:58 GMT
    Content-Type: text/plain
    Content-Length: 0

If a user attempts to delete the account of another user.

    HTTP/1.1 401 Unauthorized
    Date: Sun, 01 Oct 2023 19:00:33 GMT
    Content-Type: text/plain
    Content-Length: 0