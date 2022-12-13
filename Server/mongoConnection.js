import { MongoClient } from "mongodb";
import { config } from "dotenv";

export async function connectToMongoDB(uri) {

    let mongoClient;

    try {
        mongoClient = new MongoClient(uri)
        console.log('Connecting to MongoDB');
        await mongoClient.connect();
        console.log('Successful connected to MongoDB!')

        return mongoClient
    }catch {
        console.error("Error during the connection to mongoDB!")
    }
}

