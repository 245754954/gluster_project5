#!/bin/bash
HOSTS="localhost.peer1 localhost.peer2 localhost.master"
SCRIPT="pwd;ls;service glusterd stop;service glusterd start"
for HOSTNAME in ${HOSTS}
        do
                ssh -l root ${HOSTNAME} "${SCRIPT}"
        done


#ip_array=("192.168.56.100" "192.168.56.70")
#user="root"
#remote_cmd="/home/test/1.sh"
#for ip in ${ip_array}
#       do
#               if[$ip="192.168.56.70"];then
#                       port="7777"
#               else
#                       port="22"
#               fi
#               ssh -t -p $port $user@$ip "remote_cmd"
#       done