# 01. 객체, 설계

> 실무 vs 이론 ?
> 
- 로버트 L. 글래스(Lobert L. Glass)는 ‘소프트웨어 그리에이티비티 2.0’에서 ‘실무 대 이론'이라는 주제로 우리에게 “이론이 먼저일까, 실무가 먼저일까?”라는 질문을 던졌다.
- 글래스의 결론을 한마디로 요역하면 이론보다 실무가 먼저라는 것이다.
- 소프트웨어 개발에서 실무가 이론보다 앞서 있는 대표적인 분야로 ‘소프트웨어 설계'와 ‘소프트웨어 유지보수'를 들 수 있다.
- 실무는 훌륭한 소프트웨어를 설계하기 위해 필요한 다양한 기법과 도구를 초기부터 성공적으로 적용하고 발전
- 설계 이론은 실무에서 반복적으로 적용되던 기법들을 이론화한 것들이 대부분
- 소프트웨어 설계와 유지보수에 중점을 두려면 이론이 아닌 실무에 초점을 맞추는 것이 효과적이다.
- 설계에 관해 설명할 때 가장 유용한 도구는 이론으로 덕지덕지 치장된 개념과 용어가 아니라 ‘코드' 그 자체이다.
- 개발자는 구체적인 코드를 만지며 손을 더럽힐 때 가장 많은 것을 얻어가는 존재다.

## 1) 티켓 판매 어플리케이션 구현하기

- 티켓 판매 어플리케이션을 구현해보자
- 소극장을 경영하는데 추천을 통해 선정된 관람객에서 공연을 무료로 관람할 수 있는 초대장을 제공한다.
- 이벤트에 당첨된 관람객과 그렇지 못한 관람객은 다른 방식으로 입장시켜야 한다.
- 이벤트에 당첨된 관람객은 초대장을 티켓으로 교환한 후에 입장할 수 있다.
- 이벤트에 당첨되지 않은 관람객은 구매해야만 입장 가능하다.
- 따라서, 관람객을 입장시키기 전에 이벤트 당첨 여부를 확인해야 하고 이벤트 당첨자가 아닌 경우에는 티켓을 판매한 후에 입장시켜야 한다.

> Invitation Class
> 

```java
package Chapter01;

import java.time.LocalDateTime;

public class Invitation {
    private LocalDateTime when;
}
```

> Ticket Class
> 

```java
package Chapter01;

public class Ticket {
    private Long fee;

    public Long getFee() {
        return fee;
    }
}
```

> Bag Class
> 

```java
package Chapter01;

public class Bag {
    private Long amount;
    private Invitation invitation;
    private Ticket ticket;
    
    public Bag(long amount) {
        this(null, amount);
    }
    
    public Bag(Invitation invitation, long amount) {
        this.invitation = invitation;
        this.amount = amount;
    }

    public boolean hasInvitation() {
        return invitation != null;
    }

    public boolean hasTicket() {
        return ticket != null;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public void minusAmount(Long amount) {
        this.amount -= amount;
    }

    public void plusAmount(Long amount) {
        this.amount += amount;
    }
}
```

- Bag 인스턴스의 상태는 현금과 초대장을 함께 보관하거나, 초대장이 없이 현금만 보관하는 두 가지 중 하나일 것이다.
- Bag의 인스턴스를 생성하는 시점에 이 제약을 강제할 수 있도록 생성자를 추가한다.

> Audience Class
> 

```java
package Chapter01;

public class Audience {
    private Bag bag;

    public Audience(Bag bag) {
        this.bag = bag;
    }

    public Bag getBag() {
        return bag;
    }
}
```

> TicketOffice
> 

```java
package Chapter01;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TicketOffice {
    private Long amount;
    private List<Ticket> tickets = new ArrayList<>();

    public TicketOffice(Long amount, Ticket ... tickets) {
        this.amount = amount;
        this.tickets.addAll(Arrays.asList(tickets));
    }

    public Ticket getTicket() {
        return tickets.remove(0);
    }

    public void minusAmount(Long amount) {
        this.amount -= amount;
    }

    public void plusAmount(Long amount) {
        this.amount += amount;
    }
}
```

> TicketSeller
> 

```java
package Chapter01;

public class TicketSeller {
    private TicketOffice ticketOffice;

    public TicketSeller(TicketOffice ticketOffice) {
        this.ticketOffice = ticketOffice;
    }

    public TicketOffice getTicketOffice() {
        return ticketOffice;
    }
}
```

> Theater Class
> 

```java
package Chapter01;

public class Theater {
    private TicketSeller ticketSeller;

    public Theater(TicketSeller ticketSeller) {
        this.ticketSeller = ticketSeller;
    }

    public void enter(Audience audience) {
        if (audience.getBag().hasInvitation()) {
            Ticket ticket = ticketSeller.getTicketOffice().getTicket();;
            audience.getBag().setTicket(ticket);
        } else {
            Ticket ticket = ticketSeller.getTicketOffice().getTicket();;
            audience.getBag().minusAmount(ticket.getFee());
            ticketSeller.getTicketOffice().plusAmount(ticket.getFee());
            audience.getBag().setTicket(ticket);
        }
    }
}
```

- 소극장은 먼저 관람객의 가방 안에 초대장이 들어 있는지 확인
- 만약 초대장이 들어 있다면 이벤트에 당첨된 관람객이므로 판매원에게서 받은 티켓을 관람객의 가방 안에 넣어준다.
- 가방 안에 초대장이 없다면 관람객의 가방에서 티켓 금액만큼을 차감한 후 매표소에 금액을 증가시키고, 관람객의 가방 안에 티켓을 넣어준다.

![](https://github.com/dheldh77/study_Object/blob/master/chapter01/classDiagram.png)

## 2) 무엇이 문제인가?

> 소프트웨어 모듈이 가져야 하는 세 가지 기능
> 

로버트 마틴에 따르면 모든 소프트웨어 모듈에는 세 가지 목적이 있다.

1. 실행 중에 제대로 동작하는 것
2. 변경을 위해 존재하는 것
3. 코드를 읽는 사람과의 의사소통하는 것

- 즉, 모든 모듈은 제대로 실행되어야 하고, 변경이 용이해야 하며, 이해하기 쉬워야 한다.
- 위 코드는 로버트 마틴이 제안한 세 가지 목적 중 ‘1. 실행 중에 제대로 동작하는 것'은 만족한다.
- 하지만 ‘2. 변경을 위해 존재하는 것’, ‘3. 코드를 읽는 사람과의 의사소통하는 것'은 만족하지 못한다.

### (1) 예상을 빗나가는 코드

- 위 코드의 문제는 Audience 객체와 TicketSeller 객체가 Theater 객체의 통제를 받는 수동적인 존재라는 점이다.
- Audience 객체와 TicketSeller 객체가 해야하는 실제 동작이 Theater 객체에 의해 동작한다.
- 이해 가능한 코드란 그 동작이 우리의 예상에서 크게 벗어나지 않는 코드다.
- 현재의 코드는 일반적인 상식과는 다르게 동작하기 때문에 코드를 읽는 사람과의 의사소통이 어렵다.
- 또한, 이 코드를 이해하기 위해서 여러가지 세부적인 내용들을 알고 있어야한다는 것이다.
- 하나의 클래스나 메서드에서 많은 세부사항을 다루기 때문에 코드를 작성하는 사람이나 읽는 사람에게 부담을 준다.

### (2) 변경에 취약한 코드

- Audience 객체가 Bag을 가지고 있다는 가정이 바뀌었다고 생각해보자. 즉, 요구 사항이 변경된 것이다.
- Audience 클래스에서 Bag을 제거해야할 뿐만 아니라 Audience의 Bag에 직접 접근하는 Theater의 enter 메서드 또한 수정되어야 한다.

> 의존성 (Dependency)
> 
- 위와 같이 하나의 변경이 연쇄적인 변경을 일으키는 이유는 객체 사이의 의존성(dependency) 때문이다.
- 의존성은 변경과 관련되어 있다.
- 의존성이라는 말 속에는 어떤 객체가 변경될 때 그 객체에게 의존하는 다른 객체도 함께 변경될 수 있다는 사실을 내포한다.
- 하지만 객체 사이의 의존성을 완전히 없애는 것이 정답은 아니다.
- 객체 지향 설계는 서로 의존하면서 협력하는 객체들의 공동체를 구축하는 것이다.
- 목표는 애플리케이션의 기능을 구현하는 데 필요한 최소한의 의존성만 유지하고 불필요한 의존성을 제거하는 것이다.

> 결합도 (Coupling)
> 
- 객체 사이의 의존성이 과한 경우를 가리켜 결합도(Coupling)가 높다고 한다.
- 결합도는 의존성과 관련되어 있기 때문에 결합도 역시 변경과 관련이 있다.
- 설계의 목표는 개체 사이의 결합도를 낮춰 변경이 용이한 설계를 만드는 것이다.

## 3) 설계 개선하기

- 변경과 의사소통은 문제가 서로 엮여있다.
- 코드를 이해하기 어려운 이유는 Theater 클래스가 Audience의 Bag과 TicketSeller의 TicketOffice에 직접 접근하기 때문이고 이로인해 변경도 어려워진다.
- 해결방법은 각 클래스르 자율적인 존재로 만드는 것이다.

### (1) 자율성을 높이자

Audience 클래스와 TicketSeller 클래스가 직접 Bag과 TicketOffice를 처리하는 자율적인 존재가 되도록 재설계하자

> Theater 클래스의 enter 메서드에서 TicketOffice에 접근하는 코드를 TicketSeller로 숨기기
> 

```java
package Chapter01;

public class TicketSeller {
    private TicketOffice ticketOffice;

    public TicketSeller(TicketOffice ticketOffice) {
        this.ticketOffice = ticketOffice;
    }

    public void sellTo(Audience audience) {
        if (audience.getBag().hasInvitation()) {
            Ticket ticket = ticketOffice.getTicket();
            audience.getBag().setTicket(ticket);
        } else {
            Ticket ticket = ticketOffice.getTicket();
            audience.getBag().minusAmount(ticket.getFee());
            ticketOffice.plusAmount(ticket.getFee());
            audience.getBag().setTicket(ticket);
        }
    }
}
```

- Theater 클래스에서 TicketSeller로 옮겨지면서 getTicketOffice 메서드가 제거되었다.
- TicketOffice에 대한 접근은 오직 TicketSeller 안에만 존재한다.
- TicketSellersms TicketOffice에 대한 동작을 스스로 수행할 수 밖에 없어진다.
- 이처럼 개념적이나 물리적으로 객체 내부의 세부사항을 감추는 것을 캡슐화(Encapsulation)라고 한다.
- 캡슐화를 통해 객체 내부로의 접근을 제한하면 객체와 객체 사이의 결합도를 낮출 수 있기 때문에 설계를 좀 더 쉽게 변경할 수 있게 된다.

```java
package Chapter01;

public class Theater {
    private TicketSeller ticketSeller;

    public Theater(TicketSeller ticketSeller) {
        this.ticketSeller = ticketSeller;
    }

    public void enter(Audience audience) {
        ticketSeller.sellTo(audience);
    }
}
```

- 변경된 Theater 클래스는 ticketOffice에 접근하지도, ticketSeller 내부에 ticketOffice에 대한 존재도 알지 못한다.
- Theater는 단지 ticketSeller가 sellTo 메시지를 이해하고 응답할 수 있다는 사실만 알고 있을 뿐이다.
- Theater는 오직 TicketSeller의 인터페이스(Interface)에만 의존한다.
- TicketSeller가 내부에 TicketOffice 인스턴스를 포함하고 있다는 사실은 구현(implementation)의 영역이다.
- 객체를 인터페이스와 구현으로 나누고 인터페이스만을 공개하는 것은 객체 사이의 결합도를 낮추고 변경하기 쉬운 코드를 작성하기 위해 따라야 하는 가장 기본적인 설계 원칙이다.

> Audience 캡슐화
> 
- TicketSeller는 Audience의 getter 메서드를 호출해서 내부의 Bag 인스턴스에 직접 접근한다.
- 현재 코드에서 Audience는 자율적인 존재가 아니다.

```java
package Chapter01;

public class Audience {
    private Bag bag;

    public Audience(Bag bag) {
        this.bag = bag;
    }

    public Long buy(Ticket ticket) {
        if (bag.hasInvitation()) {
            bag.setTicket(ticket);
            return 0L;
        } else {
            bag.setTicket(ticket);
            bag.minusAmount(ticket.getFee());
            return ticket.getFee();
        }
    }
}
```

- 변경된 코드는 Audience에서 직접 Bag 객체에 접근해 초대장이 있는지 여부를 확인한다.
- 이로인해 Bag의 존재가 내부로 캡슐화된 것이다.

```java
package Chapter01;

public class TicketSeller {
    private TicketOffice ticketOffice;

    public TicketSeller(TicketOffice ticketOffice) {
        this.ticketOffice = ticketOffice;
    }

    public void sellTo(Audience audience) {
        ticketOffice.plusAmount(audience.buy(ticketOffice.getTicket()));
    }
}
```

- TicketSeller가 Audience의 인터페이스에만 의존하도록 수정했다.
- 수정 결과 TicketSeller와 Audience 사이의 결합도는 낮아지고, 각 코드의 수정이 서로에게 영향을 끼치지 않는다.

![](https://github.com/dheldh77/study_Object/blob/master/chapter01/classDiagram2.png)

- 재설계 결과로 Theater 클래스의 TicketOffice, Ticket, Bag의 의존은 제거되었다.
- 하지만, TicketSeller에서 Ticket으로의 의존이 생겼다.
- 앞서 말햇지만, 모든 의존을 제거하는 것은 옳지 않다.
- 의존 관계는 트레이드오프하다.
- Theater와 Ticket의 의존과 TicketSeller와 Ticket의 의존 중 뭐가 더 현명한 선택일까?

### (3) 무엇이 개선됐는가

- 수정된 코드의 Audience와 TicketSeller는 자신이 가지고 있는 소지품을 직접 관리한다.
- 이는 일반적인 관점과 동일하며 코드를 읽는 사람과의 의사소통이라는 관점에서 코드가 개선되었다.
- 또한, 두 클래스의 내부 구현이 변경되더라도 Theater 클래스를 변경할 필요가 없어졌다.
- 따라서 로버트 마틴의 모듈의 세 가지 조건을 만족하도록 변경된 것이다.

### (4) 어떻게 한 것인가

- 우리의 직관에 따라 자신의 문제를 스스로 해결하도록 코드를 변경했고, 코드는 변경이 용이하고 이해 가능하도록 수정되었다.
- 객체의 자율성을 높이는 방향으로 설계를 개선하므로써, 이해하기 쉽고 유연한 설계를 얻을 수 있었다.

### (5) 캡슐화와 응집도

- 핵심은 객체 내부의 상태를 캡슐화하고 객체 간에 오직 메시지를 통해서만 상호작용하도록 만드는 것이다.
- 밀접하게 연관된 작업만을 수행하고 연광성 없는 작업은 다른 객체에게 위임하는 객체를 가리켜 응집도(cohesion)이 높다고 말한다.
- 자신의 데이터를 스스로 처리하는 자율적인 객체를 만들면 결합도를 낮출 수 있을뿐더러 응집도를 높일 수 있다.
- 객체의 응집도를 높이기 위해서는 객체 스스로 자신의 데이터를 책임져야 한다.
- 외부의 간섭을 최대한 배제하고 메시지를 통해서만 협력하는 자율적인 객체들의 공동체를 만드는 것이 훌륭한 객체지향 설계를 얻을 수 있는 지름길이다.

### (6) 절차지향과 객체지향

> 절차적 프로그래밍 (Procedural Programming)
> 
- 프로세스와 데이터를 별도의 모듈에 위치시키는 방식을 절차적 프로그래밍(Procedural Programming)이라고 부른다.
- 절차적 프로그래밍 방식으로 작성된 코드는 모든 처리가 하나의 클래스 안에 위치하고 나머지 클래스는 단지 데이터의 역할만 수행하게 된다.
- 일반적으로 절차적 프로그래밍은 우리의 직관에 위배되기 때문에 코드를 읽는 사람과 의사소통이 어렵다.
- 또한, 절차적 프로그래밍은 데이터의 변경으로 인한 영향을 지역적으로 고립시키기 어렵다.
- 변경하기 쉬운 설계는 한 번에 하나의 클래스만 변경할 수 있는 설계다.
- 절차적 프로그래밍은 프로세스가 필요한 모든 데이터에 의존해야 한다는 근본적인 문제점 때문에 변경에 취약하다.

> 객체지향 프로그래밍 (Object-Oriented Programming)
> 
- 데이터와 프로세스가 동일한 모듈 내부에 위치하도록 프로그래밍하는 방식을 객체지향 프로그래밍이라 한다.
- 객체지향 프로그래밍은 의존성은 적절히 통제되고 하나의 변경으로 인한 여파가 여러 클래스로 전파되는 것을 억제한다.
- 객체지향 설계의 핵심은 캡슐화를 이용해 의존성을 적절히 관리함으로써 객체 사이의 결합도를 낮추는 것이다.

### (7) 책임의 이동

- 절차적 프로그래밍과 객체지향 프로그래밍 방식 사이에 근본적인 차이를 만드는 것은 **책임의 이동**이다.
- ‘책임'은 기능을 가리키는 객체지향의 용어로 생각해도 무방하다.
- 두 방식의 차이를 쉽게 이해할 수 있는 방법을 기능을 처리하는 방법이다.
- 위 코드 예제에서 변경 전 코드는 Theater에 책임이 집중되어 있다.
- 변경 후 책임이 각 객체에 적절하게 분산되었다.(책임의 이동)
- 객체지향 설계에서는 하나의 기능을 완성하는 데 필요한 책임이 여러 객체에 걸쳐 분산된다.
- 객체지향 애플리케이션은 스스로 책임을 수행하는 자율적인 객체들의 공동체를 구성함으로써 완성된다.
- 객치지향은 단순히 데이터와 프로세스를 하나의 객체 안으로 모으는 것 이상의 의미를 가진다.
- 객체지향 설계의 핵심은 적절한 객체에 적절한 책임을 할당하는 것이다.

> 정리
> 
- 설계를 어렵게 만드는 것은 의존성이다.
- 불필요한 의존성을 제거함으로써 객체 사이의 결합도를 낮추는 것이 해결방법이다.
- 캡슐화는 결합도를 낮추기 위한 방법 중 하나이다.
- 불필요한 세부사항을 캡슐화하는 자율적인 객체들이 낮은 결합도와 높은 응집도를 가지고 협력하도록 최소한의 의존성만을 남기는 것이 훌륭한 객체지향 설계다.

### (8) 더 개선할 수 있다

> Bag 클래스 캡슐화
> 
- Bag 클래스는 Audience에 의해 동작하는 수동적인 존재다.
- Bag의 내부 상태에 접근하는 모든 로직을 Audience에서 Bag 클래스 안으로 캡슐화해서 결합도를 낮춰보자

```java
package Chapter01;

public class Bag {
    private Long amount;
    private Invitation invitation;
    private Ticket ticket;

    public Bag(long amount) {
        this(null, amount);
    }

    public Bag(Invitation invitation, long amount) {
        this.invitation = invitation;
        this.amount = amount;
    }

    private boolean hasInvitation() {
        return invitation != null;
    }

    public boolean hasTicket() {
        return ticket != null;
    }

    private void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    private void minusAmount(Long amount) {
        this.amount -= amount;
    }

    public void plusAmount(Long amount) {
        this.amount += amount;
    }

    public Long hold(Ticket ticket) {
        if (hasInvitation()) {
            setTicket(ticket);
            return 0L;
        } else {
            setTicket(ticket);
            minusAmount(ticket.getFee());
            return ticket.getFee();
        }
    }
}
```

- Audience 클래스의 buy 메서드에서 Bag에 직접 접근하는 로직을 Bag 내부로 캡슐화했다.
- Audience 클래스에서 접근하던 메서드는 Bag 내부에서만 접근하도록 수정했기 때문에 접근 제어자도 public에서 private로 수정할 수 있다.

```java
package Chapter01;

public class Audience {
    private Bag bag;

    public Audience(Bag bag) {
        this.bag = bag;
    }

    public Long buy(Ticket ticket) {
        return bag.hold(ticket);
    }
}
```

- Audience는 인터페이스에만 의존하게 된다.

> TicketOffice 클래스 캡슐화
> 
- TicketOffice는 TicketSeller에의해 내부의 Ticket이 접근되고 있다.
- TicketSeller가 TicketOffice 내부의 Ticket에 접근하는 코드를 Ticket 내부로 감춰보자

```java
package Chapter01;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TicketOffice {
    private Long amount;
    private List<Ticket> tickets = new ArrayList<>();

    public TicketOffice(Long amount, Ticket ... tickets) {
        this.amount = amount;
        this.tickets.addAll(Arrays.asList(tickets));
    }

    private Ticket getTicket() {
        return tickets.remove(0);
    }

    public void minusAmount(Long amount) {
        this.amount -= amount;
    }

    private void plusAmount(Long amount) {
        this.amount += amount;
    }

    public void sellTicketTo(Audience audience) {
        plusAmount(audience.buy(getTicket()));
    }
}
```

```java
package Chapter01;

public class TicketSeller {
    private TicketOffice ticketOffice;

    public TicketSeller(TicketOffice ticketOffice) {
        this.ticketOffice = ticketOffice;
    }

    public void sellTo(Audience audience) {
        ticketOffice.sellTicketTo(audience);
    }
}
```

- 재설계를 통해 TicketSeller가 인터페이스에만 의존해서 TicketOffice에 접근하고 있다.
- 하지만 이로인해 존재하지 않았던 의존성이 추가되었다.

![변경 전](https://github.com/dheldh77/study_Object/blob/master/chapter01/classDiagram3.png)

변경 전

![변경 후](https://github.com/dheldh77/study_Object/blob/master/chapter01/classDiagram4.png)

변경 후

- 의존성의 추가는 높은 결합도를 의미하고, 높은 결합도는 변경하기 어려운 설계를 의미한다.
- TicketOffice의 자율성은 높였지만 전체 설계 관점에서 결합도가 높아졌다.
- 어떤  기능을 설계하는 방법은 한 가지 이상일 수 있고, 설계는 한 가지 이상의 방법에 대해 트레이드오프의 산물이다.
- 어떤 경우에도 모든 사람들을 만족시킬 수 있는 설계를 만들 수는 없다.

### (9) 그래, 거짓말이다!

- 앞에서 현실세계의 생물처럼 스스로 생각하고 행동하도록 소프트웨어 객체를 설계하는 것이 이해하기 쉬운 코드를 작성하는 것이라고 설명했다.
- 앞의 코드의 Bag, Theater, TicketOffice는 현실세계의 수동적인 존재라고 하더라도 일단 객체지향의 관점에서는 모든 것이 능동적이고 자율적인 존재이다.
- 이처럼 능동적이고 자율적인 존재로 소프트웨어 객체를 설계하는 원칙을 가리켜 의인화(anthropomorphism)라고 부른다.
- 훌륭한 객체지향 설계란 소프트웨어를 구성하는 모든 객체들이 자율적으로 행동하는 설계를 가리킨다.

## 4) 객체지향 설계

### (1) 설계가 왜 필요한가

- 설계란 코드를 배치하는 것이다.
- 설계를 구현과 떨어트려서 이야기하는 것은 불가능하다.
- 설계는 코드를 작성하는 매 순간 코드를 어떻게 배치할 것인지 결정하는 과정에서 나온다.
- 설계는 코드 작성의 일부이며 코드를 작성하지 않고서는 검증할 수 없다.

> 좋은 설계란?
> 
- 오늘 완성해야하는 기능을 구현하는 코드를 짜야 하는 동시에 내일 쉽게 변경할 수 있는 코드를 짜야한다.
- **좋은 설계란 오늘 요구하는 기능을 온전히 수행하면서 내일의 변경을 매끄럽게 수용할 수 있는 설계다.**
- 변경을 수용할 수 있는 설계가 중요한 이유는 요구사항이 항상 변경되기 때문이다.

### (2) 객체지향 설계

- 우리가 진정으로 원하는 것은 변경에 유연하게 대응할 수 있는 코드다.
- 객체지향 프로그래밍은 의존성을 효율적으로 통제할 수 있는 다양한 방법을 제공함으로써 요구사항 변경에 좀 더 수월하게 대응할 수 있는 가능성을 높여준다.
- 변경 가능한 코드란 이해하기 쉬운 코드다.
- 객체지향 패러다임은 세상을 바라보는 방식대로 코드를 작성할 수 있게 돕느다.
- 세상에 존재하는 모든 자율적인 존재처럼 객체 역시 자신의 데이터를 스스로 책임지는 자율적인 존재다.
- 훌륭한 객체지향 설계란 협력하는 객체 사이의 의존성을 적절하게 관리하는 설계다.
- 객체 간의 의존성은 애플리케이션을 수정하기 어렵게 만드는 주범이다.
- 진정한 객체지향 설계는 데이터와 프로세스를 하나의 덩어리로 모으고, 협력하는 개체들 사이의 의존성을 적절하게 조절함으로써 변경에 용이한 설계를 만드는 것이다.
