# SMS Consumer - Android App

Android application that consumes messages from Confluent Cloud Kafka and sends SMS to phone numbers.

## Features

- ✅ Connects to Confluent Cloud Kafka cluster
- ✅ Consumes SMS messages from Kafka topic
- ✅ Sends SMS using device SIM
- ✅ Start/Stop service from UI
- ✅ Background service for continuous message consumption
- ✅ Real-time message processing

## Prerequisites

- Android Studio 2022.1 or later
- Android SDK 21 (API Level 21) or higher
- Confluent Cloud account with Kafka cluster
- API Key and Secret from Confluent Cloud
- Android device with SMS capability

## Quick Setup

### 1. Clone the Repository
```bash
git clone https://github.com/vkhub1908/LibraryApp-SMSConsumer.git
cd LibraryApp-SMSConsumer
```

### 2. Open in Android Studio
1. Open Android Studio
2. File → Open → Select the project folder
3. Wait for Gradle sync to complete

### 3. Update Confluent Cloud Credentials

Edit `app/src/main/java/com/library/smsconsumer/config/KafkaConfig.java`:

```java
public static final String BOOTSTRAP_SERVERS = "pkc-YOUR_REGION.confluent.cloud:9092";
public static final String API_KEY = "YOUR_API_KEY";
public static final String API_SECRET = "YOUR_API_SECRET";
public static final String TOPIC_NAME = "student-expiring-plans";
```

**How to find these values:**

1. **Bootstrap Servers**: 
   - Go to Confluent Cloud Console → Your Cluster → Cluster Settings
   - Copy the Bootstrap server URL
   - Example: `pkc-abc123.us-east-1.provider.confluent.cloud:9092`

2. **API Key & Secret**: 
   - Go to Confluent Cloud Console → API Keys
   - Create new or use existing credentials
   - Copy the API Key and Secret

3. **Topic Name**: 
   - Match your Kafka topic name (default: `student-expiring-plans`)

### 4. Build and Run

1. Connect Android device via USB (or use Android Emulator)
2. Enable USB Debugging on device (Settings → Developer Options)
3. Click **Run** button (green play icon) in Android Studio
4. Select your device and click OK

### 5. Grant Permissions

When app launches, grant:
- ✅ SMS permission
- ✅ Internet permission

## Usage

1. Open the app
2. Click **Start SMS Consumer**
3. Status will show: "Service Started - Listening to Kafka..."
4. App will listen for messages from Kafka topic
5. When message arrives, SMS will be sent to the phone number

## Message Format

Messages from Kafka should be in JSON format:

```json
{
  "number": "+1234567890",
  "message": "Your library plan expires in 3 days"
}
```

## Testing

### Produce a Test Message

Using Confluent Cloud UI or CLI:

```bash
kafka-console-producer --broker-list YOUR_BOOTSTRAP_SERVER \
  --topic student-expiring-plans \
  --property parse.key=true \
  --property key.separator=:

# Type and press Enter:
key:{"number":"+1234567890","message":"Your plan expires in 3 days"}
```

Or using Confluent Cloud UI:
1. Go to Topics → student-expiring-plans → Messages
2. Produce a message with value:
```json
{"number":"+1234567890","message":"Your plan expires in 3 days"}
```

## Project Structure

```
app/src/main/
├── java/com/library/smsconsumer/
│   ├── MainActivity.java                 # Main UI Activity
│   ├── config/
│   │   └── KafkaConfig.java             # Kafka configuration
│   ├── service/
│   │   └── KafkaConsumerService.java    # Background Kafka consumer
│   ├── model/
│   │   └── SmsMessage.java              # SMS message data model
│   └── util/
│       └── SmsHelper.java                # SMS sending utility
└── res/
    ├── layout/
    │   └── activity_main.xml
    └── values/
        ├── colors.xml
        ├── strings.xml
        └── themes.xml
```

## Dependencies

- **Kafka Client**: `org.apache.kafka:kafka-clients:3.7.0`
- **JSON**: `org.json:json:20230227`
- **AndroidX**: `androidx.appcompat:appcompat:1.6.1`
- **Material Design**: `com.google.android.material:material:1.9.0`

## Troubleshooting

### Connection Issues
- ❌ "Failed to connect to broker"
  - Verify BOOTSTRAP_SERVERS URL is correct
  - Check API Key and Secret are accurate
  - Ensure device has internet connection
  - Verify Kafka topic name matches

### SMS Not Sending
- ❌ "Permission denied for sending SMS"
  - Grant SMS permission when prompted
  - Check: Settings → Apps → SMS Consumer → Permissions → Send SMS

### Build Issues
- ❌ "Gradle sync failed"
  - File → Invalidate Caches → Restart
  - Build → Clean Project
  - Build → Rebuild Project

## Logs

Check logs in Android Studio Logcat:

```bash
adb logcat | grep -E "KafkaConsumerService|SmsHelper|MainActivity"
```

Or in Android Studio:
1. View → Tool Windows → Logcat
2. Filter by: `KafkaConsumerService`, `SmsHelper`, or `MainActivity`

## Architecture

- **MainActivity**: Handles UI and permission requests
- **KafkaConsumerService**: Background service that runs indefinitely, consuming messages from Kafka
- **KafkaConfig**: Centralized Confluent Cloud configuration
- **SmsMessage**: Data model for SMS messages
- **SmsHelper**: Utility for sending SMS via device SIM

## Permissions Required

- `SEND_SMS`: To send SMS messages
- `INTERNET`: To connect to Kafka broker
- `ACCESS_NETWORK_STATE`: To monitor internet connectivity

## License

MIT License - Feel free to use and modify

## Support

- For Confluent Cloud issues: https://support.confluent.io
- For Android issues: Check Android Studio Logcat
- For this app: Open an issue on GitHub
