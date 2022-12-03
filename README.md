# Nice Places Android app

![Downloads on Google Play](https://img.shields.io/badge/downloads-1.2K-success)
![GitHub repo size](https://img.shields.io/github/repo-size/niceplaces/android-app)
![GitHub issues](https://img.shields.io/github/issues/niceplaces/android-app)

## Discover the culture around you

**Nice Places helps you to discover the places of interest in your area briefly describing their history and characteristics.**

![Nice Places screenshots](https://github.com/niceplaces/.github/blob/main/profile/devices-en.png)

This project was born in 2018 from the idea of two students from Siena, Italy.  Our mission is to create a mobile application to help users discover historical and cultural places of interest in their vicinity and to provide brief information about them.

*Nice Places is available as [Android app](https://play.google.com/store/apps/details?id=com.niceplaces.niceplaces) and as a [progressive web app](https://www.niceplaces.it/en/app/) in both Italian and English.*

In 2022, the project became open source to allow anyone to contribute.

[Join the Discord channel](https://discord.gg/p9fC72mzDX)

## Copyright notice

The source code available in this repository is licensed under the [MIT license](https://github.com/niceplaces/android-app/blob/main/LICENSE), however this repository contains digital assets to which the following terms apply.

*The logos, names and texts of [Pro Loco Sovicille](http://www.prolocosovicille.it/), [Cammino d'Etruria](https://www.facebook.com/camminodetruria/) and [Pro Loco Murlo](https://prolocomurlo.it/) are used with the permission of the respective associations and belong to them.*

*The profile photos of people appearing on the site are used under their permission and belong to them.*

*The contents mentioned above MUST NOT be used outside the Nice Places project.*

## 🛠️ Installation steps

1. Clone the repository.

    ```bash
    git clone https://github.com/niceplaces/android-app.git niceplaces-android-app
    ```

1. Get an API key for Google Maps for Android following this guide: [Using API Keys  |  Maps SDK for Android  |  Google Developers](https://developers.google.com/maps/documentation/android-sdk/get-api-key).

1. Create the file `google_maps_api.xml` in folder `app/src/main/res/values/` with the following content:

    ```xml
    <resources>
        <string name="google_maps_key" templateMergeStrategy="preserve" translatable="false">[YOUR_API_KEY]</string>
    </resources>
    ```

1. Run the app!