all: clean

clean: 
	find . -name '*.log' -type f -exec rm -f {} \; 

run:
	node app.js

docker-build: 
	docker build -t nodejs .
	docker images

docker-clean:
	docker stop nodejs
	docker rm nodejs

docker-shell:
	docker exec -it starbucks bash 

network-create:
	docker network create --driver bridge starbucks

network-inspect:
	docker network inspect starbucks

docker-run:
	docker run --network risky --name starbucks-nodejs -p 8080:8080  -e "api_endpoint=http://starbucks-api:3000" -td nodejs

docker-setup:
	docker network create --driver bridge risky

docker-launch:
	docker run --network risky --name starbucks-nodejs -p 8080:8080 
	-e "api_endpoint=http://starbucks-api:3000" -td nodejs