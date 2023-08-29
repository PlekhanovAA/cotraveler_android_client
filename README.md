<h1>coTraveler android client</h1>

СoTraveler is an android application for finding travel companions within a city or a certain geographical area.
The work of this client is impossible without the server part, which is located in this [repository](https://github.com/PlekhanovAA/cotraveler_server).

<h3>Preparation</h3>

1. Take the code and fix the server address in com/cotraveler/androidapp/utils/URIHelper.java:123 to the correct one.
2. Compile the code, build an **APK** file and upload it to your smartphone.

<h3>Launch</h3>

Click on the coTraveler app icon on your smartphone.

<h3>Guide</h3>

After launching the application on the Splash Screen, select the role.

If you are a **driver**, then to find co travelers, do:
* select the starting point on the map, which is located along the route of your journey
* check out the list of orders for which the selected point is a point from
* choose from the list of orders here, the point to which will also be part of your route
* the points from and to will be marked on the map
* you can also read the passenger's comment, if there is one
* click RESET FILTERS to start a new search for co travelers

If you are a **passenger**, then to create an order, do:

* select the route points from and to on the map
  * <em>restriction: no more than 10 orders per point</em>
* if necessary, add a comment to the order
* click SUBMIT and wait
  * <em>restriction: order lifetime is 15 minutes</em>
* the driver who travels along the points of your route will see your order, and maybe you will become co travellers
* if your route is finished or you no longer need to look for co travelers, click OFFER DONE

