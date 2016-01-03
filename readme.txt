To set-up project:

1. Import project by gradle model

2. Create res/values/secret.xml file and define your Google Maps API key and OKAPI Key
<resources>
    <string name="mapsKey" translatable="false">YOUR_SECRET_GOOGLE_MAPS_KEY</string>
    <string name="okapiKey_pl" translatable="false">YOUR_SECRET_OKAPI_KEY_PL</string>
    <string name="okapiKey_de" translatable="false">YOUR_SECRET_OKAPI_KEY_DE</string>
    <string name="okapiKey_us" translatable="false">YOUR_SECRET_OKAPI_KEY_US</string>
</resources>

3. (optional) Create local.properties file and define path to your sdk:
sdk.dir=D\:\\ADT\\sdk