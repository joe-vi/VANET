# Study and Analysis of MAC Protocols in VANET

## Overview

This project investigates the implementation and performance evaluation of Medium Access Control (MAC) protocols in Vehicular Ad hoc Networks (VANETs). VANETs are decentralized wireless networks enabling vehicle-to-vehicle (V2V) and vehicle-to-infrastructure (V2I) communication, crucial for applications like accident prevention and autonomous driving.

---

## Project Background

- The Data Link Layer manages network access for nodes.
- Wired networks use Ethernet and CSMA/CD for collision management.
- VANETs are wireless ad hoc networks without fixed infrastructure, presenting challenges for collision domain management.
- Communication types in VANET:
  - Inter-vehicle (multi-hop multicast/broadcast)
  - Vehicle-to-roadside (single-hop broadcast)
- VANET MAC protocols must handle diverse node densities, high vehicle speed (up to 160 km/h), and dynamic topologies.

---

## Objectives

- Study, implement, and analyze three MAC protocols used in VANETs.
- Propose improvements to enhance Quality of Service (QoS).
- Address different VANET scenarios including urban, rural, and varied node densities.

---

## Tools and Technologies

- **OpenStreetMap (OSM):** Source for real-world geospatial data.
- **SUMO:** Microscopic traffic simulator for route and traffic generation.
- **NS-3:** Network simulator for communication protocol evaluation.
- **Java:** Implemented MAC protocols and network simulation logic.
- Linux/Unix OS for compatibility.

---

## System Architecture

- Real-world traffic is modeled using OSM and SUMO.
- NS-3 is used for network layer simulations.
- Java handles MAC layer implementation, including packet generation, collision detection, and time-slot management.
- Vehicles are grouped dynamically each simulation second based on spatial proximity.

---

## Project Modules

- OpenStreetMap: Real-world map data generation.
- SUMO: Traffic simulation.
- NS-3: Network and routing protocol simulation.
- Java: MAC protocol implementation, packet handling, simulation control, and performance metrics calculation.

---

## Simulation Process

1. **Vehicle Grouping:** Vehicles within a certain radius form communication groups every second.
2. **Random Event Generation:** Vehicles generate packets with randomly assigned events affecting packet size and timing.
3. **Network Implementation:** Simulation of packet transmission, collision detection, retransmissions, and dropping.
4. **Performance Metrics:** Throughput, delivery ratio, delay, and group formation statistics calculated over multiple simulation runs.

---

## Mobility Generation Steps

1. Export map data of interest (e.g., Kakkanad, Cochin) from OpenStreetMap.
2. Convert `.osm` map file to SUMO network file using:
   ```netconvert --osm-files /path/to/map.osm --lefthand -o /path/to/map.net.xml```
3. Create polygon files for enhanced road and object representation:
   ```polyconvert --net-file /path/to/map.net.xml --osm-files /path/to/map.osm --type-file /path/to/typemap.xml -o /path/to/map.poly.xml```
4. Generate vehicle trips (.trips.xml) with SUMO’s randomTrips.py:
   ```python /path/to/sumo/tools/randomTrips.py -n /path/to/map.net.xml -e 100 -l```
5. Generate route files (.rou.xml) to define vehicle routes:
```python /path/to/sumo/tools/randomTrips.py -n /path/to/map.net.xml -r /path/to/map.rou.xml -e 100 -l```
6. Prepare the SUMO configuration file (`map.sumo.cfg`) by copying and modifying an example config.
7. Visualize simulation in SUMO GUI:
   ```sumo-gui /path/to/map.sumo.cfg```
8. Generate vehicle movement trace for MAC protocol simulation:
   ```sumo -c map.sumo.cfg --fcd-output sumoTrace.xml```

---

## Running Java MAC Protocol Simulation

- Use the generated `sumoTrace.xml` as input.
- Compile and run the Java simulation program (`XmlRead.java` and related classes).
- Output performance metrics will be saved in `output.csv`.

---

## Results and Analysis

- IEEE 802.11p outperforms 802.11b in speed and interference resilience.
- Modified MAC protocol shows improved throughput and delivery ratio due to efficient time slot usage and collision handling.
- As vehicle count rises, modified MAC performs better by reducing packet drops and delay.

---

## Future Scope

- Enhanced RSU transfer protocols.
- Improved RSU to OBU communications.
- More precise packet generation models.
- Better time slot utilization for reduced collision and delay.

---

## Challenges

- Realistic vehicular mobility modeling is complex.
- Collaborative development posed difficulties during the COVID-19 pandemic.

---

## Research Implications

- Confirms preference for 802.11p as VANET standard due to performance benefits.
- Validates benefits of adaptive CSMA/CA-based MAC protocols.
- Highlights ongoing necessity for diverse MAC protocol research for varying VANET scenarios.

---

## References

- [NS-3 Documentation](https://www.nsnam.org/documentation/)
- [OpenStreetMap](https://www.openstreetmap.org/#map=4/21.84/82.79)
- [SUMO Tutorials](https://sumo.dlr.de/docs/Tutorials.html)
- Related research papers on VANET MAC protocols.

---

**Thank you for exploring this VANET MAC protocol research project!**
