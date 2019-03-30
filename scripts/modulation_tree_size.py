# Convert all data into bytes

nodeSize = 32 # 256 bits
segSize = 64 * 1024 # 64 KB

KBUnit = 'KB'
KBSize = 1024 # 1 KB

MBUnit = 'MB'
MBSize = 1024 * 1024 # 1 MB

GBUnit = 'GB'
GBSize = 1024 * 1024 * 1024 # 1GB

def treesize(size):
	num = size / segSize
	return (num * 2 - 1) * 256


fs = []
size = 1 # MB
for i in range(10):
	size = size * 2
	fs.append(size * MBSize)

for size in fs:
	ts = treesize(size) # in B
	print 'FileSize = ' + str(size / MBSize) + ' ' + MBUnit + ',\tTreeSize = ' + str(ts / KBSize) + ' ' + KBUnit + ',\tRatio = ' + str(float(size)/ts)


fs = []
size = 1 # MB
for i in range(10):
	size = size * 2
	fs.append(size * GBSize)

for size in fs:
	ts = treesize(size) # in B
	print 'FileSize = ' + str(size / GBSize) + ' ' + GBUnit + ',\tTreeSize = ' + str(ts / MBSize) + ' ' + MBUnit + ',\tRatio = ' + str(float(size)/ts)



