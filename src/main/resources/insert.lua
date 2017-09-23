local mainKey = 'WEATHER-INFO-MAIN-INDEX'
local cityKey = 'WEATHER-INFO-CITY-INDEX-'
local weatherKey = 'WEATHER-INFO-WEATHER-INDEX-'
local levelKey = 'WEATHER-INFO-LEVEL-INDEX-'

local addRet = redis.call('SADD', mainKey, ARGV[1])
if addRet == 1 then
    redis.call('SADD', cityKey .. KEYS[1], ARGV[1])
    redis.call('SADD', weatherKey .. KEYS[2], ARGV[1])
    redis.call('SADD', levelKey .. KEYS[3], ARGV[1])
end

return addRet