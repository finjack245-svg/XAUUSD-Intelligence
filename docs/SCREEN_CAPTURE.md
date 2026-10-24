# Screen capture and privacy

Use Android MediaProjection only after the user explicitly requests capture and accepts the system prompt.

## Required behaviour
- Request consent for every new capture session.
- Show an in-app capture-active state and a clear Stop button.
- Handle `MediaProjection.Callback.onStop()` and release the virtual display, image reader/surface, and other resources.
- Never silently restart capture after process death or system revocation.
- Do not save screenshots by default.
- Do not transmit screenshots to a server.
- If a later feature proposes sharing screenshots, require separate, explicit consent and document exactly what is sent.

## Android 14+
The app targets Android 14+ compatible behaviour. The manifest must declare the `mediaProjection` foreground-service type and the corresponding foreground-service permissions when a foreground service is used. Follow the current Android Developers MediaProjection guidance, including the one-session/one-token restrictions.

Reference: https://developer.android.com/media/grow/media-projection
