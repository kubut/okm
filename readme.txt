To set-up project:

1. Import project by gradle model

2. Create res/values/secret.xml file and define your Google Maps API key and OKAPI Key
<resources>
    <string name="mapsKey">YourSecretKey</string>
    <string name="okapiKey">YourSecretOKAPIKey</string>
</resources>

3. (optional) Create local.properties file and define path to your sdk:
sdk.dir=D\:\\ADT\\sdk