To set-up project:

1. Import project by gradle model

2. Create res/values/secret.xml file and define your Google Maps API key and OKAPI Key
<resources xmlns:tools="http://schemas.android.com/tools">
    <string name="mapsKey" translatable="false">YOUR_SECRET_GOOGLE_MAPS_KEY</string>
    <string name="okapiKey_pl" translatable="false">YOUR_SECRET_OKAPI_KEY_PL</string>
    <string name="okapiKey_de" translatable="false">YOUR_SECRET_OKAPI_KEY_DE</string>
    <string name="okapiKey_us" translatable="false">YOUR_SECRET_OKAPI_KEY_US</string>
</resources>

3. (optional) Create local.properties file and define path to your sdk:
sdk.dir=D\:\\ADT\\sdk

4. (optional) Configure your AVD to Google Maps (new AVD with Google API works with Google Maps OOTB)
- Create new AVD (no Google API!)
- Install 2 packages: com.android.vending and com.google.android.gms:
adb -e install <apk>
- Restart AVD