#!/bin/bash
java -Dsimulation=true -DdataSource.dataSet=1year -cp classes:lib/* com.algoTrader.starter.SimulationStarter simulateWithCurrentParams
