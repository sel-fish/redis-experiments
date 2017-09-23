local mainKey = 'WEATHER-INFO-MAIN-INDEX'
local cityKey = 'WEATHER-INFO-CITY-INDEX-'
local weatherKey = 'WEATHER-INFO-WEATHER-INDEX-'
local levelKey = 'WEATHER-INFO-LEVEL-INDEX-'

local remRet = redis.call('SREM', mainKey, ARGV[1])
if remRet == 1 then
    redis.call('SREM', cityKey .. KEYS[1], ARGV[1])
    redis.call('SREM', weatherKey .. KEYS[2], ARGV[1])
    redis.call('SREM', levelKey .. KEYS[3], ARGV[1])
end

return remRet
