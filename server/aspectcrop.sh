#!/bin/bash

aspect=""
gravity="center"

# set directory for temporary files
dir="."    # suggestions are dir="." or dir="/tmp"

# set up functions to report Usage and Usage with Description
PROGNAME=`type $0 | awk '{print $3}'`  # search for executable on path
PROGDIR=`dirname $PROGNAME`            # extract directory of program
PROGNAME=`basename $PROGNAME`          # base name of program
usage1() 
	{
	echo >&2 ""
	echo >&2 "$PROGNAME:" "$@"
	sed >&2 -e '1,/^####/d;  /^###/g;  /^#/!q;  s/^#//;  s/^ //;  4,$p' "$PROGDIR/$PROGNAME"
	}
usage2() 
	{
	echo >&2 ""
	echo >&2 "$PROGNAME:" "$@"
	sed >&2 -e '1,/^####/d;  /^######/g;  /^#/!q;  s/^#*//;  s/^ //;  4,$p' "$PROGDIR/$PROGNAME"
	}


# function to report error messages
errMsg()
	{
	echo ""
	echo $1
	echo ""
	usage1
	exit 1
	}


# function to test for minus at start of value of second part of option 1 or 2
checkMinus()
	{
	test=`echo "$1" | grep -c '^-.*$'`   # returns 1 if match; 0 otherwise
    [ $test -eq 1 ] && errMsg "$errorMsg"
	}

# test for correct number of arguments and get values
if [ $# -eq 0 ]
	then
	# help information
   echo ""
   usage2
   exit 0
elif [ $# -gt 6 ]
	then
	errMsg "--- TOO MANY ARGUMENTS WERE PROVIDED ---"
else
	while [ $# -gt 0 ]
		do
			# get parameter values
			case "$1" in
		     -help)    # help information
					   echo ""
					   usage2
					   exit 0
					   ;;
				-a)    # get aspect
					   shift  # to get the next parameter
					   # test if parameter starts with minus sign 
					   errorMsg="--- INVALID ASPECT SPECIFICATION ---"
					   checkMinus "$1"
					   aspect=`expr "$1" : '\([.0-9]*[:]*[.0-9]*\)'`
					   [ "$aspect" = "" ] && errMsg "--- ASPECT=$aspect MUST BE ONE OR TWO FLOATS GREATER THAN OR EQUAL TO 0 SEPARATED BY A COLON ---"
					   ;;
				-g)    # get  gravity
					   shift  # to get the next parameter
					   # test if parameter starts with minus sign 
					   errorMsg="--- INVALID GRAVITY SPECIFICATION ---"
					   checkMinus "$1"
					   gravity="$1"
					   gravity=`echo "$gravity" | tr "[:upper:]" "[:lower:]"`
					   case "$gravity" in 
					   		center|c) gravity="center" ;;
					   		north|n) gravity="north" ;;
					   		south|s) gravity="south" ;;
					   		east|e) gravity="east" ;;
					   		west|w) gravity="west" ;;
					   		northwest|nw) gravity="northwest" ;;
					   		northeast|nw) gravity="northeast" ;;
					   		southwest|sw) gravity="southwest" ;;
					   		southeast|se) gravity="southeast" ;;
					   		*) errMsg "--- GRAVITY=$gravity IS AN INVALID VALUE ---" 
					   	esac
					   ;;
				 -)    # STDIN and end of arguments
					   break
					   ;;
				-*)    # any other - argument
					   errMsg "--- UNKNOWN OPTION ---"
					   ;;
		     	 *)    # end of arguments
					   break
					   ;;
			esac
			shift   # next option
	done
	#
	# get infile and outfile
	infile="$1"
	outfile="$2"
fi

# test that infile provided
[ "$infile" = "" ] && errMsg "NO INPUT FILE SPECIFIED"

# test that outfile provided
[ "$outfile" = "" ] && errMsg "NO OUTPUT FILE SPECIFIED"

# setup temporary images
tmpA1="$dir/aspectcrop_1_$$.mpc"
tmpA2="$dir/aspectcrop_1_$$.cache"
trap "rm -f $tmpA1 $tmpA2;" 0
trap "rm -f $tmpA1 $tmpA2; exit 1" 1 2 3 15
trap "rm -f $tmpA1 $tmpA2; exit 1" ERR


# read the input image and test validity.
convert -quiet "$infile" +repage "$tmpA1" ||
	errMsg "--- FILE $infile DOES NOT EXIST OR IS NOT AN ORDINARY FILE, NOT READABLE OR HAS ZERO SIZE  ---"

# get size and aspect ratio of input
ww=`convert $tmpA1 -ping -format "%w" info:`
hh=`convert $tmpA1 -ping -format "%h" info:`
ratio=`convert xc: -format "%[fx:$ww/$hh]" info:`
#echo "ww=$ww; hh=$hh ratio=$ratio;"


# copy input to output if aspect is not specified 
if [ "$aspect" = "" ]; then
	convert $tmpA1 $outfile
	exit
fi

# get aspect
aspect1=`echo $aspect | cut -d\: -f1`
aspect2=`echo $aspect | cut -d\: -f2`
test=`convert xc: -format "%[fx:($aspect2 == $aspect1)?1:0]" info:`
#echo "aspect1=$aspect1; aspect2=$aspect2;"
if [ $aspect1 -eq 0 -o $aspect2 -eq 0 ]; then
	errMsg "--- DESIRED WIDTH OR HEIGHT MUST NOT BE ZERO ---"
elif [ "$aspect2" = "" ]; then
	aspect=$aspect1
elif [ $test -eq 1 ]; then 
	aspect=$aspect1
else
	aspect=`convert xc: -format "%[fx:$aspect1/$aspect2]" info:`
fi
#echo "aspect=$aspect;"



# test if aspect >= ratio
test=`convert xc: -format "%[fx:$aspect>=$ratio?1:0]" info:`
[ $test -eq 1 ] && format="larger" || format="smaller" 
#echo "format=$format;"

# compute width and height of output
if [ "$format" = "larger" ]; then
	width=$ww
	height=`convert xc: -format "%[fx:$hh*$ratio/$aspect]" info:`
elif [ "$format" = "smaller" ]; then
	width=`convert xc: -format "%[fx:$ww*$aspect/$ratio]" info:`
	height=$hh
fi	


# process image
convert $tmpA1 -gravity $gravity -crop ${width}x${height}+0+0 +repage "$outfile"
convert $outfile -resize 600x600 $outfile

exit 0

