GET ALL ITEMS
-------------
curl http://localhost:8081/items/retrieve
curl http://localhost:8081/items/exchange

GET ITEM BY ID
--------------
curl http://localhost:8081/items/5/retrieve
curl http://localhost:8081/items/5/exchange

CREATE ITEM
-----------
curl -d '{"id":null,"description":"Item #X","price":999.99}' -H "Content-Type: application/json" -X POST http://localhost:8081/items

UPDATE ITEM BY ID
-----------------
curl -d '{"id":null,"description":"ITEM #6","price":129.99}' -H "Content-Type: application/json" -X PUT http://localhost:8081/items/6

DELETE ITEM
-----------
curl -X "DELETE" http://localhost:8081/items/5