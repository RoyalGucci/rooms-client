# rooms-client

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

1. **Clone the repository:**
    ```shell
    git clone https://github.com/RoyalGucci/rooms-client.git
    cd rooms-client
    ```
2. **Start the server:**
    ```shell
    ./gradlew run
    ```