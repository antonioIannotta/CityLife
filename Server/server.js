const express = require('express');
import { readLocationForEveryUser } from './mongoOperations';

const app = express();
app.use('cors')

const port = 3000;
let mapUserLocation = []
let interestedUsers = []

app.get('/', (req, res) => {
    res.send('Hello World from express!');
});

app.listen(port, () => {
    
    //console.log("Server is using port " + port);


});



/**
 * Funzione che si occupa di recuperare, ad ogni segnalazione ricevuta, i dati sulla posizione e distanza di interesse memorizzati
 * nel DB da parte degli utenti
 */
function retrieveLocationForEveryUser() {
    mapUserLocation = []
    let listOfDocuments = readLocationForEveryUser();
        let singleUserLocationMap = {};
        listOfDocuments.forEach(
            function(document) {
                singleUserLocationMap.set("Username", document._id);
                singleUserLocationMap.set("Location", document["Location"]);
                singleUserLocationMap.set("Distance", document["Distance"]);
                mapUserLocation.push(singleUserLocationMap)
                singleUserLocationMap = {}
            }
        )
}


function interestedUsersForReport(report) {
    let reportLocation = report["Location"]
    let reportLatitude = reportLocation.split(" - ")[0];
    let reportLongitude = reportLocation.split(" - ")[1];

    for (let userLocation in mapUserLocation) {
        let userLatitude = userLocation["Latitude"].split(" - ")[0];
        let userLongitude = userLocation["Longitude"].split(" - ")[1];
        let userPreferredDistance = userLocation["Distance"];

        if (Math.abs(getDistanceFromLatLonInKm(reportLatitude, userLatitude, reportLongitude, userLongitude)) < Math.abs(userPreferredDistance)) {
            interestedUsers.push(userLocation["Username"]);
        }
    }

    for (let user in interestedUsers) {
        send(user, report);
    }

}


function send(user, report) {
    //TODO
}

function getDistanceFromLatLonInKm(lat1, lon1, lat2, lon2) {
    var earthRadius = 6371;
    var dLat = deg2rad(lat2 - lat1);
    var dLon = deg2rad(lon2 - lon1);
    var a =
        Math.sin(dLat / 2) * Math.sin(dLat / 2) + 
        Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
        Math.sin(dLon / 2) * Math.sin(dLon / 2);

    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return earthRadius * c;
}

function deg2rad(deg) {
    return deg * (Math.PI / 180)
}