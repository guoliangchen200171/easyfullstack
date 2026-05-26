local current = redis.call('GET', KEYS[1])
if not current then
    return -1
end
local stock = tonumber(current)
local qty = tonumber(ARGV[1])
if stock >= qty then
    return redis.call('DECRBY', KEYS[1], qty)
else
    return -1
end
