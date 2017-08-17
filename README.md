stock-management
===

# Run

```
$ sbt dist
[..]
$ unzip target/universal/stock-management-1.0.0-SNAPSHOT.zip 
[..]
$ stock-management-1.0.0-SNAPSHOT/bin/stock-management -Dplay.http.secret.key='QCY?tAnfk?aZ?iwrNwnxIlR6CTf:G3gf:90Latabg@5241AB`R5W:1uDFN];Ik@n'
```

# Example request

### POST /product

```
curl -X POST \
  http://localhost:9000/product \
  -H 'content-type: application/json' \
  -d '{
	"name": "Foo"
}'
```

### GET /product/:id

```
curl -X GET \
  http://localhost:9000/product/1 
```

### POST /product/:id/refill

```
curl -X POST \
  http://localhost:9000/product/1/refill 
```

### POST /product/:id/buy

```
curl -X POST \
  http://localhost:9000/product/1/buy 
```

### POST /product/:id/reserve

```
curl -X POST \
  http://localhost:9000/product/1/reserve 
```
