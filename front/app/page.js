'use client';
import { useEffect } from 'react';

export default function Home() {
  useEffect(() => {
    // Mobile menu toggle
    const mobileMenuBtn = document.querySelector('.mobile-menu-btn');
    const navLinks = document.querySelector('.nav-links');
    const ctaButtons = document.querySelector('.cta-buttons');

    const toggleMenu = () => {
      if (navLinks.style.display === 'flex') {
        navLinks.style.display = 'none';
        ctaButtons.style.display = 'none';
      } else {
        navLinks.style.display = 'flex';
        ctaButtons.style.display = 'flex';
        
        // Make it responsive
        navLinks.style.flexDirection = 'column';
        navLinks.style.position = 'absolute';
        navLinks.style.top = '70px';
        navLinks.style.left = '0';
        navLinks.style.right = '0';
        navLinks.style.backgroundColor = 'var(--white)';
        navLinks.style.padding = '20px';
        navLinks.style.boxShadow = 'var(--shadow)';
        
        ctaButtons.style.flexDirection = 'column';
        ctaButtons.style.position = 'absolute';
        ctaButtons.style.top = '220px';
        ctaButtons.style.left = '0';
        ctaButtons.style.right = '0';
        ctaButtons.style.backgroundColor = 'var(--white)';
        ctaButtons.style.padding = '20px';
        ctaButtons.style.boxShadow = 'var(--shadow)';
      }
    };

    mobileMenuBtn?.addEventListener('click', toggleMenu);

    return () => {
      mobileMenuBtn?.removeEventListener('click', toggleMenu);
    };
  }, []);

  return (
    <>
      {/* Header */}
      <header>
        <div className="container">
          <div className="header-content">
            <a href="#" className="logo">
              <div className="logo-icon">MB</div>
              <div className="logo-text">MyBudget</div>
            </a>
            
            <nav className="nav-links">
              <a href="#features" className="nav-link">Funcionalidades</a>
              <a href="#how-it-works" className="nav-link">Como Funciona</a>
              <a href="#pricing" className="nav-link">Preços</a>
              <a href="#testimonials" className="nav-link">Depoimentos</a>
              <a href="#faq" className="nav-link">FAQ</a>
            </nav>
            
            <div className="cta-buttons">
              <a href="/login" className="btn btn-outline">Entrar</a>
              <a href="/register" className="btn btn-primary">Cadastrar</a>
            </div>
            
            <button className="mobile-menu-btn">☰</button>
          </div>
        </div>
      </header>

      {/* Hero Section */}
      <section className="hero">
        <div className="container">
          <h1>Controle suas finanças de forma simples e eficiente</h1>
          <p>MyBudget é a solução completa para gerenciar seu dinheiro, acompanhar gastos e alcançar seus objetivos financeiros.</p>
          <div className="hero-buttons">
            <a href="/login" className="btn btn-primary">Começar Agora</a>
            <a href="#features" className="btn btn-outline">Conhecer Mais</a>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section id="features" className="section">
        <div className="container">
          <h2 className="section-title">Funcionalidades Poderosas</h2>
          <p className="section-subtitle">Descubra todas as ferramentas que vão transformar sua relação com o dinheiro</p>
          
          <div className="features-grid">
            <div className="feature-card">
              <div className="feature-icon">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                  <line x1="12" y1="1" x2="12" y2="23"></line>
                  <path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"></path>
                </svg>
              </div>
              <h3 className="feature-title">Dashboard Intuitivo</h3>
              <p className="feature-description">Acompanhe seu saldo, receitas e despesas em tempo real com um painel visual e fácil de entender.</p>
            </div>
            
            <div className="feature-card">
              <div className="feature-icon">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                  <rect x="1" y="4" width="22" height="16" rx="2" ry="2"></rect>
                  <line x1="1" y1="10" x2="23" y2="10"></line>
                </svg>
              </div>
              <h3 className="feature-title">Relatórios Detalhados</h3>
              <p className="feature-description">Gere relatórios completos com gráficos e análises para entender seus padrões de gastos.</p>
            </div>
            
            <div className="feature-card">
              <div className="feature-icon">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                  <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"></path>
                </svg>
              </div>
              <h3 className="feature-title">Modo Escuro</h3>
              <p className="feature-description">Use o aplicativo no modo escuro para reduzir a fadiga visual durante a noite.</p>
            </div>
            
            <div className="feature-card">
              <div className="feature-icon">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                  <path d="M12 20h9"></path>
                  <path d="M16.5 3.5a2.121 2.121 0 0 1 3 3L7 19l-4 1 1-4L16.5 3.5z"></path>
                </svg>
              </div>
              <h3 className="feature-title">Gestão de Transações</h3>
              <p className="feature-description">Adicione, edite e exclua transações de forma rápida e organizada.</p>
            </div>
            
            <div className="feature-card">
              <div className="feature-icon">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                  <path d="M22 12h-4l-3 9L9 3l-3 9H2"></path>
                </svg>
              </div>
              <h3 className="feature-title">Filtros por Período</h3>
              <p className="feature-description">Visualize suas finanças por mês, trimestre ou ano com filtros personalizáveis.</p>
            </div>
            
            <div className="feature-card">
              <div className="feature-icon">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                  <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
                  <polyline points="7 10 12 15 17 10"></polyline>
                  <line x1="12" y1="15" x2="12" y2="3"></line>
                </svg>
              </div>
              <h3 className="feature-title">Exportação de Dados</h3>
              <p className="feature-description">Exporte seus relatórios em PDF para compartilhar com contadores ou para arquivamento.</p>
            </div>
          </div>
          
          {/* Dashboard Preview */}
          <div className="dashboard-preview">
            <div className="dashboard-header">
              <div className="dashboard-title">Dashboard - MyBudget</div>
              <div className="dashboard-controls">
                <div className="dashboard-control red"></div>
                <div className="dashboard-control yellow"></div>
                <div className="dashboard-control green"></div>
              </div>
            </div>
            <div className="dashboard-content">
              <div className="dashboard-sidebar">
                <div style={{marginBottom: '20px', padding: '10px', backgroundColor: 'rgba(255,255,255,0.1)', borderRadius: '6px'}}>Dashboard</div>
                <div style={{marginBottom: '10px', padding: '10px'}}>Transações</div>
                <div style={{marginBottom: '10px', padding: '10px'}}>Relatórios</div>
              </div>
              <div>
                <div className="dashboard-main">
                  <div className="stat-card balance">
                    <div className="stat-title">Saldo Atual</div>
                    <div className="stat-value">R$ 5.240,00</div>
                  </div>
                  <div className="stat-card income">
                    <div className="stat-title">Receitas</div>
                    <div className="stat-value">R$ 7.500,00</div>
                  </div>
                  <div className="stat-card expense">
                    <div className="stat-title">Despesas</div>
                    <div className="stat-value">R$ 2.260,00</div>
                  </div>
                </div>
                <div className="chart-container">
                  {/* Chart placeholder */}
                  <div style={{display: 'flex', alignItems: 'flex-end', height: '100%', gap: '10px'}}>
                    <div style={{flex: 1, backgroundColor: 'rgba(34, 197, 94, 0.8)', height: '80%', borderRadius: '4px'}}></div>
                    <div style={{flex: 1, backgroundColor: 'rgba(239, 68, 68, 0.8)', height: '60%', borderRadius: '4px'}}></div>
                    <div style={{flex: 1, backgroundColor: 'rgba(34, 197, 94, 0.8)', height: '90%', borderRadius: '4px'}}></div>
                    <div style={{flex: 1, backgroundColor: 'rgba(239, 68, 68, 0.8)', height: '70%', borderRadius: '4px'}}></div>
                    <div style={{flex: 1, backgroundColor: 'rgba(34, 197, 94, 0.8)', height: '85%', borderRadius: '4px'}}></div>
                    <div style={{flex: 1, backgroundColor: 'rgba(239, 68, 68, 0.8)', height: '50%', borderRadius: '4px'}}></div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* How It Works */}
      <section id="how-it-works" className="section">
        <div className="container">
          <h2 className="section-title">Como Funciona</h2>
          <p className="section-subtitle">Em apenas 4 passos simples, você terá total controle sobre suas finanças</p>
          
          <div className="steps">
            <div className="step">
              <div className="step-number">1</div>
              <h3 className="step-title">Cadastre-se</h3>
              <p className="step-description">Crie sua conta em menos de 2 minutos com e-mail e senha.</p>
            </div>
            
            <div className="step">
              <div className="step-number">2</div>
              <h3 className="step-title">Adicione Transações</h3>
              <p className="step-description">Registre suas receitas e despesas de forma rápida e organizada.</p>
            </div>
            
            <div className="step">
              <div className="step-number">3</div>
              <h3 className="step-title">Acompanhe o Dashboard</h3>
              <p className="step-description">Visualize seu saldo e gastos em tempo real com gráficos intuitivos.</p>
            </div>
            
            <div className="step">
              <div className="step-number">4</div>
              <h3 className="step-title">Analise Relatórios</h3>
              <p className="step-description">Gere relatórios detalhados para tomar melhores decisões financeiras.</p>
            </div>
          </div>
        </div>
      </section>

      {/* Pricing
      <section id="pricing" className="section">
        <div className="container">
          <h2 className="section-title">Planos e Preços</h2>
          <p className="section-subtitle">Escolha o plano ideal para suas necessidades financeiras</p>
          
          <div className="pricing-grid">
            <div className="pricing-card">
              <h3 className="pricing-name">Básico</h3>
              <div className="pricing-price">Grátis</div>
              <div className="pricing-period">para sempre</div>
              
              <ul className="pricing-features">
                <li className="pricing-feature available">Até 50 transações/mês</li>
                <li className="pricing-feature available">Dashboard básico</li>
                <li className="pricing-feature">Relatórios em PDF</li>
                <li className="pricing-feature">Suporte prioritário</li>
                <li className="pricing-feature">Backup automático</li>
              </ul>
              
              <a href="#" className="btn btn-outline" style={{width: '100%'}}>Começar Agora</a>
            </div>
            
            <div className="pricing-card featured">
              <h3 className="pricing-name">Premium</h3>
              <div className="pricing-price">R$ 19,90</div>
              <div className="pricing-period">por mês</div>
              
              <ul className="pricing-features">
                <li className="pricing-feature available">Transações ilimitadas</li>
                <li className="pricing-feature available">Dashboard completo</li>
                <li className="pricing-feature available">Relatórios em PDF</li>
                <li className="pricing-feature available">Suporte prioritário</li>
                <li className="pricing-feature">Backup automático</li>
              </ul>
              
              <a href="#" className="btn btn-primary" style={{width: '100%'}}>Experimente Grátis</a>
            </div>
            
            <div className="pricing-card">
              <h3 className="pricing-name">Empresarial</h3>
              <div className="pricing-price">R$ 49,90</div>
              <div className="pricing-period">por mês</div>
              
              <ul className="pricing-features">
                <li className="pricing-feature available">Transações ilimitadas</li>
                <li className="pricing-feature available">Dashboard completo</li>
                <li className="pricing-feature available">Relatórios em PDF</li>
                <li className="pricing-feature available">Suporte prioritário</li>
                <li className="pricing-feature available">Backup automático</li>
              </ul>
              
              <a href="#" className="btn btn-outline" style={{width: '100%'}}>Fale Conosco</a>
            </div>
          </div>
        </div>
      </section> */}

      {/* Testimonials
      <section id="testimonials" className="section testimonials">
        <div className="container">
          <h2 className="section-title">O que nossos usuários dizem</h2>
          <p className="section-subtitle">Mais de 10.000 pessoas já transformaram suas finanças com o MyBudget</p>
          
          <div className="testimonials-grid">
            <div className="testimonial-card">
              <p className="testimonial-text">&quot;Finalmente consegui organizar minhas finanças! O MyBudget me mostrou para onde estava indo meu dinheiro e agora consigo economizar 30% do meu salário.&quot;</p>
              <div className="testimonial-author">
                <div className="author-avatar">MC</div>
                <div className="author-info">
                  <h4>Maria Clara</h4>
                  <p>Designer, 28 anos</p>
                </div>
              </div>
            </div>
            
            <div className="testimonial-card">
              <p className="testimonial-text">&quot;Como freelancer, tinha dificuldade em controlar entradas e saídas. Com o MyBudget, consigo prever meus ganhos e planejar melhor meus gastos.&quot;</p>
              <div className="testimonial-author">
                <div className="author-avatar">RS</div>
                <div className="author-info">
                  <h4>Ricardo Silva</h4>
                  <p>Desenvolvedor, 32 anos</p>
                </div>
              </div>
            </div>
            
            <div className="testimonial-card">
              <p className="testimonial-text">&quot;Os relatórios me ajudaram a identificar gastos desnecessários. Em 3 meses já reduzi minhas despesas em 25% sem perder qualidade de vida.&quot;</p>
              <div className="testimonial-author">
                <div className="author-avatar">AF</div>
                <div className="author-info">
                  <h4>Ana Ferreira</h4>
                  <p>Professora, 41 anos</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section> */}

      {/* FAQ */}
      <section id="faq" className="section testimonials">
        <div className="container">
          <h2 className="section-title">Perguntas Frequentes</h2>
          <p className="section-subtitle">Tire suas dúvidas sobre o MyBudget</p>
          
          <div className="faq-grid">
            <div>
              <div className="faq-item">
                <h3 className="faq-question">Meus dados estão seguros?</h3>
                <p className="faq-answer">Sim, utilizamos criptografia de ponta a ponta e não compartilhamos seus dados com terceiros. Sua privacidade é nossa prioridade.</p>
              </div>
              
              <div className="faq-item">
                <h3 className="faq-question">Posso usar no celular?</h3>
                <p className="faq-answer">Sim, o MyBudget é totalmente responsivo e funciona perfeitamente em smartphones, tablets e computadores.</p>
              </div>
              
              <div className="faq-item">
                <h3 className="faq-question">Como faço para cancelar?</h3>
                <p className="faq-answer">Você pode cancelar sua assinatura a qualquer momento diretamente na sua conta, sem burocracia ou taxas adicionais.</p>
              </div>
            </div>
            
            <div>
              <div className="faq-item">
                <h3 className="faq-question">Posso importar dados do meu banco?</h3>
                <p className="faq-answer">Atualmente não temos integração direta com bancos, mas você pode importar arquivos CSV ou inserir as transações manualmente.</p>
              </div>
              
              <div className="faq-item">
                <h3 className="faq-question">Há limite de transações?</h3>
                <p className="faq-answer">No plano gratuito há limite de 50 transações por mês. Nos planos Premium e Empresarial as transações são ilimitadas.</p>
              </div>
              
              <div className="faq-item">
                <h3 className="faq-question">Oferecem suporte em português?</h3>
                <p className="faq-answer">Sim, nosso suporte é 100% em português e está disponível por e-mail e chat para todos os usuários.</p>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="section">
        <div className="container">
          <div className="cta">
            <h2>Pronto para transformar suas finanças?</h2>
            <p>Junte-se a milhares de usuários que já alcançaram seus objetivos financeiros com o MyBudget.</p>
            <a href="/login" className="btn btn-primary" style={{padding: '14px 32px', fontSize: '16px'}}>Começar Agora</a>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer>
        <div className="container">
          <div className="footer-grid">
            <div>
              <div className="footer-logo">
                <div className="logo-icon">MB</div>
                <div className="logo-text">MyBudget</div>
              </div>
              <p className="footer-description">MyBudget é a solução completa para gerenciar seu dinheiro, acompanhar gastos e alcançar seus objetivos financeiros.</p>
              <div className="social-links">
                <a href="#" className="social-link">
                  <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                    <path d="M18 2h-3a5 5 0 0 0-5 5v3H7v4h3v8h4v-8h3l1-4h-4V7a1 1 0 0 1 1-1h3z"></path>
                  </svg>
                </a>
                <a href="#" className="social-link">
                  <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                    <rect x="2" y="2" width="20" height="20" rx="5" ry="5"></rect>
                    <path d="M16 11.37A4 4 0 1 1 12.63 8 4 4 0 0 1 16 11.37z"></path>
                    <line x1="17.5" y1="6.5" x2="17.51" y2="6.5"></line>
                  </svg>
                </a>
                <a href="#" className="social-link">
                  <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                    <path d="M23 3a10.9 10.9 0 0 1-3.14 1.53 4.48 4.48 0 0 0-7.86 3v1A10.66 10.66 0 0 1 3 4s-4 9 5 13a11.64 11.64 0 0 1-7 2c9 5 20 0 20-11.5a4.5 4.5 0 0 0-.08-.83A7.72 7.72 0 0 0 23 3z"></path>
                  </svg>
                </a>
              </div>
            </div>
            
            <div>
              <h3 className="footer-heading">Produto</h3>
              <ul className="footer-links">
                <li className="footer-link"><a href="#features">Funcionalidades</a></li>
                {/* <li className="footer-link"><a href="#pricing">Preços</a></li>
                <li className="footer-link"><a href="#testimonials">Depoimentos</a></li> */}
                <li className="footer-link"><a href="#faq">FAQ</a></li>
              </ul>
            </div>
            
            {/* <div>
              <h3 className="footer-heading">Empresa</h3>
              <ul className="footer-links">
                <li className="footer-link"><a href="#">Sobre Nós</a></li>
                <li className="footer-link"><a href="#">Blog</a></li>
                <li className="footer-link"><a href="#">Carreiras</a></li>
                <li className="footer-link"><a href="#">Contato</a></li>
              </ul>
            </div> */}
            
            <div>
              <h3 className="footer-heading">Legal</h3>
              <ul className="footer-links">
                <li className="footer-link"><a href="#">Termos de Uso</a></li>
                <li className="footer-link"><a href="#">Política de Privacidade</a></li>
                <li className="footer-link"><a href="#">Cookies</a></li>
              </ul>
            </div>
          </div>
          
          <div className="footer-bottom">
            <p>&copy; 2025 MyBudget. Todos os direitos reservados.</p>
          </div>
        </div>
      </footer>
    </>
  );
}