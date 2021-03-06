#include "car_engine.h"

#include "stm32f4_discovery.h"
#include "stm32f4xx_conf.h"

#define CAR_ENGINE_ENABLE

#define CAR_ENGINE_GPIO_PORT_CLOCK RCC_AHB1Periph_GPIOB
#define CAR_ENGINE_GPIO_PORT GPIOB
#define CAR_ENGINE_ENABLE_PIN GPIO_Pin_3
#define CAR_ENGINE_LWHEEL_FWD_PIN GPIO_Pin_4
#define CAR_ENGINE_LWHEEL_BKWD_PIN GPIO_Pin_5
#define CAR_ENGINE_RWHEEL_FWD_PIN GPIO_Pin_6
#define CAR_ENGINE_RWHEEL_BKWD_PIN GPIO_Pin_7

void car_engine_init(void)
{
    GPIO_InitTypeDef GPIO_InitStructure;
    RCC_AHB1PeriphClockCmd(CAR_ENGINE_GPIO_PORT_CLOCK, ENABLE);
    GPIO_InitStructure.GPIO_Pin = CAR_ENGINE_ENABLE_PIN
        | CAR_ENGINE_LWHEEL_FWD_PIN
        | CAR_ENGINE_LWHEEL_BKWD_PIN
        | CAR_ENGINE_RWHEEL_FWD_PIN
        | CAR_ENGINE_RWHEEL_BKWD_PIN;
    GPIO_InitStructure.GPIO_Mode = GPIO_Mode_OUT;
    GPIO_InitStructure.GPIO_OType = GPIO_OType_PP;
    GPIO_InitStructure.GPIO_Speed = GPIO_Speed_2MHz;
    GPIO_InitStructure.GPIO_PuPd = GPIO_PuPd_NOPULL;
    GPIO_Init(CAR_ENGINE_GPIO_PORT, &GPIO_InitStructure);
}

void car_engine_stop(void)
{
    GPIO_ResetBits(CAR_ENGINE_GPIO_PORT, 0
            | CAR_ENGINE_LWHEEL_BKWD_PIN
            | CAR_ENGINE_ENABLE_PIN
            | CAR_ENGINE_LWHEEL_FWD_PIN
            | CAR_ENGINE_RWHEEL_FWD_PIN
            | CAR_ENGINE_RWHEEL_BKWD_PIN);
}

#ifdef CAR_ENGINE_ENABLE
void car_engine_forward(void)
{
    car_engine_stop();
    GPIO_SetBits(CAR_ENGINE_GPIO_PORT, CAR_ENGINE_ENABLE_PIN);
    GPIO_SetBits(CAR_ENGINE_GPIO_PORT, CAR_ENGINE_LWHEEL_FWD_PIN);
    GPIO_SetBits(CAR_ENGINE_GPIO_PORT, CAR_ENGINE_RWHEEL_FWD_PIN);
}

void car_engine_backward(void)
{
    car_engine_stop();
    GPIO_SetBits(CAR_ENGINE_GPIO_PORT, CAR_ENGINE_ENABLE_PIN);
    GPIO_SetBits(CAR_ENGINE_GPIO_PORT, CAR_ENGINE_LWHEEL_BKWD_PIN);
    GPIO_SetBits(CAR_ENGINE_GPIO_PORT, CAR_ENGINE_RWHEEL_BKWD_PIN);
}

void car_engine_turn_right(void)
{
    car_engine_stop();
    GPIO_SetBits(CAR_ENGINE_GPIO_PORT, CAR_ENGINE_ENABLE_PIN);
    GPIO_SetBits(CAR_ENGINE_GPIO_PORT, CAR_ENGINE_LWHEEL_BKWD_PIN);
    GPIO_SetBits(CAR_ENGINE_GPIO_PORT, CAR_ENGINE_RWHEEL_FWD_PIN);
}

void car_engine_turn_left(void)
{
    car_engine_stop();
    GPIO_SetBits(CAR_ENGINE_GPIO_PORT, CAR_ENGINE_ENABLE_PIN);
    GPIO_SetBits(CAR_ENGINE_GPIO_PORT, CAR_ENGINE_LWHEEL_FWD_PIN);
    GPIO_SetBits(CAR_ENGINE_GPIO_PORT, CAR_ENGINE_RWHEEL_BKWD_PIN);
}
#else
void car_engine_forward(void) {}
void car_engine_backward(void) {}
void car_engine_turn_right(void) {}
void car_engine_turn_left(void) {}
#endif // CAR_ENGINE_ENABLE
