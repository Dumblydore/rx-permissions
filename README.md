# RxPermissions
(Another) library that wraps android permissions in a reactive api

[![](https://jitpack.io/v/Dumblydore/rx-permissions.svg)](https://jitpack.io/#Dumblydore/rx-permissions)

## Setup
`minSdkVersion` must be at least 15
   ```
   allprojects {
       repositories {
           ...
           maven { url 'https://jitpack.io' }
       }
   }
   implementation 'com.github.Dumblydore:rx-permissions:1.0.0'
   ```
## Usage
Kotlin: `FragmentActivity` and `Fragment` use the extension function `requestPermissions(vararg String)`

Java: `RxPermission.requestPermissions(Fragment, ...String)` or `RxPermission.requestPermissions(FragmentActivity, ...String)`
   
`requestPermissions()` returns a `PermissionSingle` which emits `List<PermissionResult>`. 

There is a helper method called `all()` that emits a `Boolean` that's `true` if all permissions were granted.  

