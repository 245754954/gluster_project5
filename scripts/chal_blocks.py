ratio  = 0.01

px = 0.999

base = 1 - ratio
prod = 1
print 'ratio\t\t#blocks\t\tprob'
for x in xrange(1, 10000):
	prod = prod * base
	curr_px = 1 - prod

	if x % 100 == 0:
		print ratio, '\t\t', x, '\t\t', curr_px

	if curr_px >= px:
		print ratio, '\t\t', x, '\t\t', curr_px
		break

GB = 1024 * 1024 * 1024
MB = 1024 * 1024
file_size = 2 * GB
block_size = 2 * MB
blocks_num = file_size / block_size

print '\n\nfile_size\tblock_size\tblocks_num'
print file_size/GB, 'GB\t\t', block_size/MB, 'MB\t\t', blocks_num


