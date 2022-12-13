const { connectToMongoDB } = require("./mongoConnection")
import { config } from "dotenv"

config()
const mongoClient = await connectToMongoDB(process.env.DB_URI);
const collection = mongoClient.db('CityLife').collection('Location');

export function readLocationForEveryUser() {
    return collection.find()
} 
