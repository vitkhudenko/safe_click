There is a common problem in Android development: users may click a view fast enough to trigger double (or even triple) click events even if
the app expects a single click event. The Android API does not guard us against this, so we have to deal with the issue on our own.

### Integration

At the project level `build.gradle`, add a maven repo pointing to `https://jitpack.io`, e.g.:

```groovy
allprojects {
    repositories {
        google()
        maven { url "https://jitpack.io" } // this is it
        jcenter()
    }
}
```

At a module level `build.gradle`, add the following dependency:

```groovy
implementation 'com.github.vitkhudenko:safe_click:1.0.1'
```

### Usage

```kotlin
val button: Button = findViewById(R.id.myButton)

button.setSafeClickListener {
    // do something
}

// or

val listener = SafeClickListener(action = { /* do something */ })
button.setOnClickListener(listener)
```

### License

> MIT License
>
> Copyright (c) 2021 Vitaliy Khudenko
>
> Permission is hereby granted, free of charge, to any person obtaining a copy
> of this software and associated documentation files (the "Software"), to deal
> in the Software without restriction, including without limitation the rights
> to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
> copies of the Software, and to permit persons to whom the Software is
> furnished to do so, subject to the following conditions:
>
> The above copyright notice and this permission notice shall be included in all
> copies or substantial portions of the Software.
>
> THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
> IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
> FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
> AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
> LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
> OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
> SOFTWARE.
