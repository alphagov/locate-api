Locate Api
==========

The Locate API is a simple API to enable access to a Mongo database containing UK address information.

## Usage

### Address Lookups

The first use case is address lookups. The basic call (curl example):

    curl -H"Authorization: Bearer credentials" https://host/locate/addresses?postcode=kt70tj
    
There is only one query parameter: 

    postcode=a11aa
    
### Queries

Each credential is associated with a query:

Example authorization mongo document:

    {
    	"_id" : ObjectId("object_id"),
    	"name" : "username",
    	"identifier" : "username@email.gov.uk",
    	"organisation" : "test org",
    	"token" : "this is my credentials",
    	"queryType" : "residential",
    	"dataType" : "all"
    }

The fields that control the query are:

    * queryType: this restricts the results.
    
    * dataType: this restricts the JSON fields in the response.

### Query Type

These are predetermined queries the API will support.

    * Residential: Returns only residential properties.
    
    * Commercial: Returns only commercial properties.
    
    * Residential And Commercial: Returns the combination of residential and commercial queries.
   
    * Electoral: Returns properties that are deemed residential for the purposes of registering to vote, residential above plus some educational, hospital etc addresses which people may reside.
    
    * All: Returns an unfiltered view of the address database.
    
### Data Type

This controls the amount of data returned for each address. 

    * Presentation: This returns the minimum data set for an address. Suitable for most web based applications.
    
        {
            "property": "Flat A",
            "street": "93 Latchmere Road",
            "locality": "Battersea",
            "town": "London",
            "area": "Wandsworth",
            "postcode": "SW11 2DR",
            "uprn": "10090499727",
            "gssCode": "E09000032"
        }
    
    - property: Contains lowest granularity, flat numbers, house names and so on
    - street: Street number plus street name
    - locality: Area within a post town
    - town: Town name
    - area: Administrative area
    - postcode: Postcode, formatted for display
    - uprn: Unique Property Reference Number
    - gssCode: Government Statistical Service code for the Local Authority this address resides in.



    * All: The returns the full data set for an address. In most cases this is not neccessary.
        {
            "gssCode": "E09000032",
            "country": "England",
            "postcode": "sw112dr",
            uprn": "100023586417",
            "createdAt": "2014-07-03",
            "presentation": {
                "property": "Arches 11 And 12",
                "street": "Latchmere Road",
                "town": "London",
                "area": "Wandsworth",
                "postcode": "SW11 2DR"
            },
            "details": {
                "usrn": "22902824",
                "isResidential": false,
                "isCommercial": true,
                "isElectoral": false,
                "isPostalAddress": true,
                "classification": "CI03",
                "state": "approved",
                "organisation": "Mwr Motors",
                "primaryClassification": "Commercial",
                "secondaryClassification": "Industrial Application",
                "file": "TQ2575.csv",
                "blpuUpdatedAt": "2004-12-21",
                "blpuCreatedAt": "2001-03-19"
            },
            "location": {
                "lat": 51.46865944238342,
                "long": -0.16305944948944998
            },
            "ordering": {
                "paoText": "ARCHES 11 AND 12",
                "street": "LATCHMERE ROAD"
            }
        }

### Authority Lookups

        {
            "postcode": "sw112dr",
            "country": "England",
            "gssCode": "E09000032",
            "name": "Wandsworth"
        }


## Authorization

The API uses Bearer token Authorization headers for access control.


## SetUp

### Prerequisites 
    * MongoDB: The locate API utilised mongo as it's datastore.
    
    * Data: The datastore must contain an addresses database. This can be built from: https://github.com/alphagov/location-data-importer

### Running

    * There are scripts in the root of the project:
        
           * ./run-api.sh - will start the API.
           
           * ./run-debug-api.sh - will start the API in debug mode.


#### Heroku

The following environment properties must be set for the application to work in Heroku. These will override the default settings in the application yaml file.

        heroku config:set ENCRYPTED=
        heroku config:set KEY=
        heroku config:set MONGO_PORT=
        heroku config:set MONGO_HOST=
        heroku config:set MONGO_DATABASE=
        heroku config:set MONGO_USER=
        heroku config:set MONGO_PASSWORD=
        heroku config:set ALLOWED_ORIGINS=
     
 
 
### License
Issued under MIT (see LISCENSE)

### Copyright
Copyright (c) 2014 HM Government