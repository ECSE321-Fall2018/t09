package ca.mcgill.ecse321.rideshare9;

public class Stop {
        public Stop(long id, float price, String stopName) {
            this.id=id;
            this.price=price;
            this.stopName=stopName;
        }

        private long id;

        public void setId(long value) {
            this.id = value;
        }


        public long getId() {
            return this.id;
        }

        private String stopName;

        public void setStopName(String value) {
            this.stopName = value;
        }

        public String getStopName() {
            return this.stopName;
        }

        private float price;

        public void setPrice(float value) {
            this.price = value;
        }

        public float getPrice() {
            return this.price;
        }


    }
