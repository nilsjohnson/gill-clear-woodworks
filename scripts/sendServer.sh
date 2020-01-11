#bin/bash

echo "sending server..."
scp -i ~/.ssh/gillcc.pem -r /home/nils/gill-clear-woodworks/server/ ubuntu@ec2-18-233-214-103.compute-1.amazonaws.com:~