# MotoMetrics

This is a repository for the Technical Test submission for PT. Inosoft Trans Sistem.

## Modules
| Name   | Description                                                                                                |
|--------|------------------------------------------------------------------------------------------------------------|
| `app`  | This module contains all user interface (UI) components. It's where the interaction with the user happens. |
| `data` | This module is dedicated to data management and business logic. It handles data processing and operations. |


## Architecture
The following diagram illustrates the interaction between the `app` and `data` modules:

![alt text](https://github.com/achmadss/MotoMetrics/blob/main/diagram.png?raw=true)
### Explanation:

The process begins with user actions in the UI, which are managed by the ViewModel. When data is needed, the ViewModel sends a request to the Repository. The Repository then takes charge of executing the necessary logic and interfacing with the Local Data Source for database operations. Throughout this process, the Repository keeps the ViewModel informed about the data's status - whether it's loading, successfully retrieved, or if there's an error. Upon receiving this information, the ViewModel updates the UI accordingly to reflect the current state or the retrieved data.