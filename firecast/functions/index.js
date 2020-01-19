const functions = require("firebase-functions");
// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
exports.PushFenceAlarm = functions.https.onRequest((request, response) => {
  const type = request.body.event.type;
  if (type === "user.entered_geofence") {
    //		const message ={
    //			notification: {
    //				title: 'USER ALERT!'
    //				body: 'Beware of mugging in your area within 2 blocks!'
    //			}
    console.log("User entered geofence");
    //		}
  } else if(type === "user.exited_geofence"){
  	console.log("User exited geofence");
  } else {
    console.log("User location changed");
  }
  response.sendStatus(200);
});
