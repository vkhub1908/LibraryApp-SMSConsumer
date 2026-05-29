# Quick Setup Guide - SMS Consumer Android App

## Step 1: Clone the Repository

```bash
git clone https://github.com/vkhub1908/LibraryApp-SMSConsumer.git
cd LibraryApp-SMSConsumer
```

## Step 2: Get Your Confluent Cloud Credentials

### Bootstrap Server
1. Go to [Confluent Cloud Console](https://confluent.cloud)
2. Select your cluster
3. Go to **Cluster Settings**
4. Copy **Bootstrap server** (looks like: `pkc-abc123.us-east-1.provider.confluent.cloud:9092`)

### API Key & Secret
1. In Confluent Cloud Console
2. Go to **API Keys**
3. Create new or copy existing
4. Copy both **API Key** and **API Secret**

## Step 3: Update Configuration

Open file: `app/src/main/java/com/library/smsconsumer/config/KafkaConfig.java`

Replace these values:

```java
public static final String BOOTSTRAP_SERVERS = "pkc-abc123.us-east-1.provider.confluent.cloud:9092";
public static final String API_KEY = "M7Q4V5H3N2P8K9D1";
public static final String API_SECRET = "sXkL9mZ3vW2qR5tY7uJ4nE6hF8gB1cD3";
public static final String TOPIC_NAME = "student-expiring-plans";
```

## Step 4: Open in Android Studio

1. Open Android Studio
2. File → Open → Select the project folder
3. Wait for Gradle sync (bottom right corner)

## Step 5: Build & Run

1. Connect Android device via USB
2. Enable USB Debugging:
   - Settings → Developer Options → USB Debugging (toggle ON)
3. In Android Studio: Run → Run 'app' (or press Shift+F10)
4. Select your device from the list

## Step 6: Grant Permissions

When app launches:
1. Grant **SMS** permission
2. Grant **Internet** permission
3. Click **Allow** on both prompts

## Step 7: Test the App

1. Click **Start SMS Consumer** button
2. Status should show: "Service Started - Listening to Kafka..."
3. Produce a test message to your Kafka topic (see below)
4. Check if SMS is received on the device

## Testing with Kafka Message

### Using Confluent Cloud UI:

1. Go to Topics → `student-expiring-plans` → Messages
2. Click "Produce a new message"
3. Paste this in the Value field:
```json
{"number":"+1234567890","message":"Your plan expires in 3 days"}
```
4. Click Produce
5. Check your Android device for SMS

### Using Command Line:

```bash
kafka-console-producer --broker-list pkc-YOUR_REGION.confluent.cloud:9092 \
  --topic student-expiring-plans \
  --property parse.key=true \
  --property key.separator=: \
  --producer-property security.protocol=SASL_SSL \
  --producer-property sasl.mechanism=PLAIN \
  --producer-property sasl.jaas.config='org.apache.kafka.common.security.plain.PlainLoginModule required username="API_KEY" password="API_SECRET";'

# Then type and press Enter:
key:{"number":"+1234567890","message":"Your plan expires in 3 days"}
```

## Common Issues & Solutions

### Issue: "Failed to connect to broker"

**Solution:**
- ✅ Verify Bootstrap server URL (check format: `pkc-...`)
- ✅ Verify API Key and Secret are correct
- ✅ Ensure device has internet connection
- ✅ Check firewall isn't blocking port 9092

### Issue: "Gradle sync failed"

**Solution:**
1. File → Invalidate Caches → Restart
2. Sync with Gradle Files (Ctrl+Alt+Y)
3. Build → Clean Project
4. Build → Rebuild Project

### Issue: "SMS not sending"

**Solution:**
- ✅ Grant SMS permission (check app permissions in device Settings)
- ✅ Verify phone number format includes country code (+1234567890)
- ✅ Ensure device has SMS capability
- ✅ Check device has SMS credits/plan

### Issue: "Permission denied"

**Solution:**
1. Open app again
2. When prompt appears, grant the permission
3. Or go to Settings → Apps → SMS Consumer → Permissions → Enable SMS

## Check Logs

To see what's happening:

1. Open Android Studio
2. View → Tool Windows → Logcat
3. Filter by: `KafkaConsumerService` or `SmsHelper`
4. You'll see:
   - Connection status
   - Received messages
   - SMS sending status
   - Any errors

## Next Steps

- [x] Setup complete!
- [ ] Test with real messages
- [ ] Deploy to production device
- [ ] Monitor logs for issues
- [ ] Build signed APK (Build → Generate Signed Bundle / APK)

## Support

- **Confluent Cloud Issues**: https://support.confluent.io
- **Android Issues**: Check Logcat in Android Studio
- **App Issues**: Create an issue on GitHub

Happy coding! 🚀
