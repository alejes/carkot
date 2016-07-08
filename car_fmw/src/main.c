#include "car_leds.h"
#include "car_engine.h"
#include "delay.h"

int main(void)
{
    /*!< At this stage the microcontroller clock setting is already configured, 
       this is done through SystemInit() function which is called from startup
       file (startup_stm32f4xx.s) before to branch to application main.
       To reconfigure the default setting of SystemInit() function, refer to
       system_stm32f4xx.c file
     */

    leds_init();
    engine_init();

    const int DELAY = 0x3FFFFF;
    while (1)
    {
        led_set(LED_GREEN, true);
        Delay(DELAY);

        engine_forward();
        led_set(LED_ORANGE, true);
        Delay(DELAY);

        engine_stop();
        led_set(LED_RED, true);
        Delay(DELAY);

        engine_backward();
        led_set(LED_BLUE, true);
        Delay(DELAY);

        engine_stop();
        led_set(LED_GREEN, false);
        led_set(LED_ORANGE, false);
        led_set(LED_RED, false);
        led_set(LED_BLUE, false);
        Delay(DELAY);
    }
}