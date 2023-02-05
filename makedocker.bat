docker image build -t chess-server:latest .
docker run -d -p 8100:8100 chess-server:latest