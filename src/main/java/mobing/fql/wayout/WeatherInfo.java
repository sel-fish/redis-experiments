package mobing.fql.wayout;

public class WeatherInfo {
    private String city;
    private String weather;
    private WeatherLevel level;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WeatherInfo)) return false;

        WeatherInfo that = (WeatherInfo) o;

        if (!city.equals(that.city)) return false;
        if (!weather.equals(that.weather)) return false;
        return level == that.level;
    }

    @Override
    public int hashCode() {
        int result = city.hashCode();
        result = 31 * result + weather.hashCode();
        result = 31 * result + level.hashCode();
        return result;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public WeatherLevel getLevel() {
        return level;
    }

    public void setLevel(WeatherLevel level) {
        this.level = level;
    }
}
