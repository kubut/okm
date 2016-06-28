#OpecachingKubutMaps#

**OKM** is an appliacation for Android smartphones that helps you during your adventure with opencaching. If you don't know what is it, you should check [Polish](https://www.opencaching.pl) or [German](https://www.opencaching.de) opencaching website. OKM is avaible in [Google Play](https://play.google.com/store/apps/details?id=com.opencachingkubutmaps) for free


### To set-up project: ###

1. Import project by gradle model
2. Create `res/values/secret.xml` file and define your Google Maps API key and OKAPI Key
```XML
<resources xmlns:tools="http://schemas.android.com/tools">
    <string name="mapsKey" translatable="false">YOUR_SECRET_GOOGLE_MAPS_KEY</string>
    <string name="okapiKey_pl" translatable="false" tools:ignore="UnusedResources">YOUR_SECRET_OKAPI_KEY_PL</string>
    <string name="okapiKey_de" translatable="false" tools:ignore="UnusedResources">YOUR_SECRET_OKAPI_KEY_DE</string>
    <string name="okapiKey_us" translatable="false" tools:ignore="UnusedResources">YOUR_SECRET_OKAPI_KEY_US</string>
</resources>
```
3. (optional) Create `local.properties` file and define path to your sdk:
`sdk.dir=C\:\\ADT\\sdk`
