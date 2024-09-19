# rooms-client

## Real-Time Chat and Activities Application Client for the Rooms Platform

This is the client component of Rooms platform built using Java, LibGDX for the UI, and Spring
Boot for the WebSocket client. The client connects to the server to enable real-time
communication between users. For the server component see
[rooms-server](https://github.com/bubbleship/rooms-server).

### Features

- Real-time messaging
- User authentication
- Chat rooms
- Private messaging
- Message history
- Activities in the form of games

### Prerequisites

- Java 21

### How to Run

1. **Ensure Java version is 21:**
    ```shell
    java -version
   ```
2. **Clone the repository:**
    ```shell
    git clone https://github.com/RoyalGucci/rooms-client.git
    cd rooms-client
    ```
3. **Under the `/assets` directory, create the file `config.cfg` and fill it with the address of
   the server. If no such file is present, `localhost:8080` would be used instead. Example
   content:**
   ```
   192.168.1.10:8080
   ```
   Under some circumstances (such as running from an IDE), the file should be placed under the
   root of the project `/`.
4. **Start the client:**
    ```shell
    ./gradlew run
    ```