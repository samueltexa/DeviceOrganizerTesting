# Device Organizer Library Testing Application

This repository contains a testing application for the Device Organizer Library, integrating its functionalities to enable Bluetooth Low Energy (BLE) device management. The application demonstrates the usage of the library in real-world scenarios with live BLE scanning and broadcasting functionalities.

## Features
- **Device Information Storage:** Organize scanned BLE device information (MAC address, RSSI, and device name) into HashMaps for efficient retrieval.
- **Logging:** Log all scanned devices to facilitate debugging and testing.
- **Device Details Retrieval:** Fetch detailed information of a specific device by its MAC address.

## Prerequisites
Ensure you have the following set up before running the application:
- **Development Environment:** Android Studio or IntelliJ IDEA.
- **Minimum SDK Version:** API Level 21 (Android 5.0 Lollipop) or higher.
- **Java Development Kit (JDK):** Version 8 or higher.
- **Device Organizer Library Dependency:** Integrated as described in the [Integration Guide](https://github.com/samueltexa/DeviceManage).

## Usage

### Real-World Integration
The application extends the library's functionality to real-world scenarios by incorporating live BLE scanning and broadcasting.

#### Workflow:
1. **Bluetooth Scanning:** Utilize Android's Bluetooth LE API to scan nearby devices and pass results to the library.
2. **Device Management:** Store the scanned device data in a HashMap for efficient tracking.
3. **Logging and Retrieval:** Log scanned devices and retrieve specific device details as needed.

## Sample Outputs
### Logged Devices:
```json
{
  "00:1A:2B:3C:4D:5E": {"rssi": 60, "deviceName": "Device1"},
  "AB:CD:EF:12:34:56": {"rssi": 50, "deviceName": "Device2"},
  "DE:AD:BE:EF:12:34": {"rssi": 45, "deviceName": "Device3"}
}
```

### Retrieved Device Details:
```
MAC Address: 00:1A:2B:3C:4D:5E
RSSI: 60
Device Name: Device1
```

## Advantages of Using the Library
- Simplified device management via HashMaps.
- Easy-to-integrate methods for live BLE scenarios.
- Lightweight and flexible design for minimal impact on existing codebases.

## Contribution
Contributions to improve or extend this application are welcome! Feel free to submit issues or pull requests to this repository.

## License
This project is licensed under the MIT License. See the `LICENSE` file for more details.

## Author
Alinda Samuel  
Mbarara University of Science and Technology  
**Course:** Software Engineering Industrial Mini Project II  
**Course Code:** SWE4106  
**Year:** 2024/2025
