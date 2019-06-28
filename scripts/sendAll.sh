#bin/bash

echo "sending server..."
scp -i ~/.ssh/gillcc.pem -r /home/nils/gill-clear-woodworks/server/ ubuntu@ec2-18-233-214-103.compute-1.amazonaws.com:~

echo "sending build dir..."
scp -i ~/.ssh/gillcc.pem -r /home/nils/gill-clear-woodworks/build/ ubuntu@ec2-18-233-214-103.compute-1.amazonaws.com:~

echo "sending public dir..."
scp -i ~/.ssh/gillcc.pem -r /home/nils/gill-clear-woodworks/public/ ubuntu@ec2-18-233-214-103.compute-1.amazonaws.com:~